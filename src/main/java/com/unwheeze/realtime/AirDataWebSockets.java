package com.unwheeze.realtime;

import com.google.gson.Gson;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.ast.Point;
import com.rethinkdb.net.Cursor;
import com.unwheeze.beans.AirData;
import com.unwheeze.beans.GeoBean;
import com.unwheeze.database.UnwheezeDb;
import com.unwheeze.database.UnwheezeDbAirData;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

@ServerEndpoint(
        value = "/realtime/airDataFlow",
        encoders = {AirDataMessageEncoder.class},
        decoders = {AirDataMessageDecoder.class})
        //configurator = com.unwheeze.realtime.ServerEndpoint.class)
public class AirDataWebSockets {

    private Thread thread;
    private Cursor cursor;
    private UnwheezeDb db = new UnwheezeDbAirData();

    private Gson gson = new Gson();

    public AirDataWebSockets() {
    }

    /**
     * Open the WS connection and initialize the airdata table changefeed in a background thread for every new user.
     * @param session
     * @throws IOException
     */
    @OnOpen
    public void onOpen(Session session) throws IOException {

        cursor = ((UnwheezeDbAirData) db).provideChangefeed();
        Runnable run = () -> {
            for (Object change : cursor) {
                String jsonHashMap = gson.toJson(change, HashMap.class);
                for (Session sess : session.getOpenSessions())
                    sess.getAsyncRemote().sendObject(gson.fromJson(jsonHashMap, AirDataMessage.class));
            }
        };

        this.thread = new Thread(run);
        this.thread.start();
        System.out.println("opened");
    }

    /**
     * OnMessage of WS accept messages as a JSON format defining an AirData.
     * The message format is defined in the ServerEndpointConfiguration.
     * @param session
     * @param message
     * @throws IOException
     */
    @OnMessage
    public void onMessage(Session session, AirData message) throws IOException {

        if (message != null) {
            Point geoloc = RethinkDB.r.point(Float.parseFloat(message.getLocation().split(",")[0])
                    , Float.parseFloat(message.getLocation().split(",")[1]));

            String[] location = message.getLocation().split(",");
            double [] geolocation = Arrays.stream(location)
                                .mapToDouble(Double::parseDouble)
                                .toArray();

            message.setGeolocation(new GeoBean(geolocation));

            ((UnwheezeDbAirData) db).putDataInCollection(message);
        }

    }

    @OnClose
    public void onClose(Session session) throws IOException {
        cursor.close();
        this.thread = null;
    }

    @OnError
    public void onError(Session session, Throwable throwable) {

    }


}
