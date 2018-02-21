package com.unwheeze.realtime;

import com.rethinkdb.model.MapObject;
import com.unwheeze.database.UnwheezeDbAuth;
import com.unwheeze.exception.NoApiKeyFoundException;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServerEndpoint extends ServerEndpointConfig.Configurator{
    @Override
    public boolean checkOrigin(String originHeaderValue) {
        return super.checkOrigin(originHeaderValue);
    }

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        Map<String,List<String>> requestHeaders = request.getHeaders();
        //TODO : Implement it with Filter class + filter webapp
        /*String authKey = requestHeaders.get("X-Api-Key").get(0);

        UnwheezeDbAuth db = new UnwheezeDbAuth();
        boolean isApiKeyinDb = false;
        super.modifyHandshake(sec, request, response);
        if(authKey.isEmpty()) {
            response.getHeaders().put(HandshakeResponse.SEC_WEBSOCKET_ACCEPT,new ArrayList<String>());
        }
        try {
            isApiKeyinDb = db.isApiKeyInDb(authKey);
        } catch(NoApiKeyFoundException e) {
            response.getHeaders().put(HandshakeResponse.SEC_WEBSOCKET_ACCEPT,new ArrayList<String>());
        }*/
        super.modifyHandshake(sec,request,response);


    }



}
