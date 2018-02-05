package com.unwheeze.realtime;

import com.google.gson.Gson;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class AirDataMessageDecoder implements Decoder.Text<AirDataMessage> {

    @Override
    public AirDataMessage decode(String s) throws DecodeException {
        if(s == null)
            throw new DecodeException(s,"Decoding string went wrong");

        Gson gson = new Gson();
        AirDataMessage airDataMessage = gson.fromJson(s,AirDataMessage.class);

        return airDataMessage;
    }

    @Override
    public boolean willDecode(String s) {
        return true; //changed to true, false by default
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
