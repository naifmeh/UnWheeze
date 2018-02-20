package com.unwheeze.realtime;

import com.google.gson.Gson;
import com.unwheeze.beans.AirData;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class AirDataMessageDecoder implements Decoder.Text<AirData> {

    @Override
    public AirData decode(String s) throws DecodeException {
        if(s == null)
            throw new DecodeException(s,"Decoding string went wrong");

        Gson gson = new Gson();
        AirData airDataMessage = gson.fromJson(s,AirData.class);

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
