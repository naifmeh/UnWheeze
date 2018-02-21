package com.unwheeze.realtime;

import com.google.gson.Gson;
import com.rethinkdb.net.Cursor;
import com.unwheeze.beans.AirData;
import com.unwheeze.database.UnwheezeDb;
import com.unwheeze.database.UnwheezeDbAirData;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Observer;

@ServerEndpoint(
        value = "/realtime/dataflow",
        encoders = {AirDataMessageEncoder.class},
        //decoders = {AirDataMessageDecoder.class},
        configurator = com.unwheeze.realtime.ServerEndpoint.class)
public class AirDataWebSockets {

    private boolean isValidKey = false;
    private String matchedKey = "[a-z0-9]{8}\\-([a-z0-9]{4}\\-){3}[a-z0-9]{12}";
    Cursor cursor;

    public AirDataWebSockets() {}

    @OnOpen
    public void onOpen(Session session) throws IOException {
        System.out.println("opened");

        UnwheezeDb db = new UnwheezeDbAirData();
        Cursor cursor = ((UnwheezeDbAirData) db).provideChangefeed();
        for(Object change : cursor) {
            session.getAsyncRemote().sendText((String)change);
            System.out.println(change);
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {

    }

    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println("closed");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {

    }


}
