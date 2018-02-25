package com.unwheeze.realtime;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.unwheeze.beans.AirData;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class AirDataMessageEncoder implements Encoder.Text<AirDataMessage>{

    @Override
    public String encode(AirDataMessage airDataMessage) throws EncodeException {
        Gson gson = new Gson();

        if(airDataMessage != null && airDataMessage.getOld_val()==null) {
            return gson.toJson(airDataMessage.getNew_val());
        }
        else if(airDataMessage != null && airDataMessage.getNew_val()==null) {
            airDataMessage.setDeleted(true);
            return gson.toJson(airDataMessage);
        }
        else if(airDataMessage != null)
            return gson.toJson(airDataMessage);
        else throw new EncodeException(airDataMessage,"AirData probably null");

    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}

