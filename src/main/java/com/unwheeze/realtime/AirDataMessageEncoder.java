package com.unwheeze.realtime;

import com.google.gson.Gson;
import com.unwheeze.beans.AirData;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class AirDataMessageEncoder implements Encoder.Text<AirData>{
    @Override
    public String encode(AirData airDataMessage) throws EncodeException {
        Gson gson = new Gson();
        String jsonMessage;

        if(airDataMessage != null)
            jsonMessage = gson.toJson(airDataMessage);
        else throw new EncodeException(airDataMessage,"AirData probably null");

        return jsonMessage;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}

