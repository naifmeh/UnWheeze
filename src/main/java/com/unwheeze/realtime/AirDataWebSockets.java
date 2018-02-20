package com.unwheeze.realtime;

import com.google.gson.Gson;
import com.unwheeze.beans.AirData;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(
        value = "/realtime/dataflow",
        encoders = {AirDataMessageEncoder.class},
        decoders = {AirDataMessageDecoder.class})
public class AirDataWebSockets {

    private boolean isValidKey = false;
    private String matchedKey = "[a-z0-9]{8}\\-([a-z0-9]{4}\\-){3}[a-z0-9]{12}";

    @OnOpen
    public void onOpen(Session session) throws IOException {
        //TODO : Check auth credentials
    }

    @OnMessage
    public void onMessage(Session session, AirData message) throws IOException {

        String msg = (new Gson()).toJson(message);
        System.out.println(msg);
        session.getAsyncRemote().sendObject(message);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println("closed");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {

    }

}
