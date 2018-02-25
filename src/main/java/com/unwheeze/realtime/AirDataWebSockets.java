package com.unwheeze.realtime;

import com.google.gson.Gson;
import com.rethinkdb.net.Cursor;
import com.unwheeze.beans.AirData;
import com.unwheeze.database.UnwheezeDb;
import com.unwheeze.database.UnwheezeDbAirData;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Observer;

@ServerEndpoint(
        value = "/realtime/dataflow",
        encoders = {AirDataMessageEncoder.class},
        decoders = {AirDataMessageDecoder.class})
        //configurator = com.unwheeze.realtime.ServerEndpoint.class)
public class AirDataWebSockets {

    private Thread thread;
    private Cursor cursor;

    private Gson gson = new Gson();

    public AirDataWebSockets() {}

    @OnOpen
    public void onOpen(Session session) throws IOException {

        UnwheezeDb db = new UnwheezeDbAirData();
        cursor = ((UnwheezeDbAirData) db).provideChangefeed();
        Runnable run = () -> {
            for (Object change : cursor) {
                String jsonHashMap = gson.toJson(change,HashMap.class);
                for(Session sess : session.getOpenSessions())
                    sess.getAsyncRemote().sendObject(gson.fromJson(jsonHashMap,AirDataMessage.class));
            }
        };

        this.thread = new Thread(run);
        this.thread.start();
        System.out.println("opened");
    }

    @OnMessage
    public void onMessage(Session session, AirData message) throws IOException {
        UnwheezeDb db = new UnwheezeDbAirData();
        if(message != null)
            ((UnwheezeDbAirData) db).putDataInCollection(message);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println("closed");
        this.thread = null;
    }

    @OnError
    public void onError(Session session, Throwable throwable) {

    }


}
