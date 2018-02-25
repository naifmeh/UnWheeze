package com.unwheeze.realtime;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.unwheeze.beans.AirData;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class AirDataMessageDecoder implements Decoder.Text<AirData> {
    private Gson gson = new Gson();

    @Override
    public AirData decode(String s) throws DecodeException {
        if(s == null)
            throw new DecodeException(s,"Decoding string went wrong");
        AirData airDataMessage = gson.fromJson(s,AirData.class);

        return airDataMessage;
    }

    @Override
    public boolean willDecode(String s) {
        try{
            gson.fromJson(s,AirData.class);
            return true;
        } catch(JsonSyntaxException e) {
            return false;
        }
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
