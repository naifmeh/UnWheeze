package com.unwheeze.realtime;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(
        value = "/realtime/dataflow",
        encoders = {AirDataMessageEncoder.class},
        decoders = {AirDataMessageDecoder.class})
public class AirDataWebSockets {

    @OnOpen
    public void onOpen(Session session) throws IOException {

    }

    @OnMessage
    public void onMessage(Session session, AirDataMessage message) throws IOException {

    }

    @OnClose
    public void onClose(Session session) throws IOException {

    }

    @OnError
    public void onError(Session session, Throwable throwable) {

    }

}
