package com.unwheeze.realtime;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

public class AirDataQueue {

    private static String EXCHANGE_NAME = "";
    private static final String HOST = ""; //TODO : A sécuriser
    private static String routingKey;
    private static final String TOPIC = "airDataUnwheeze";
    AirDataMessage airDataMessage;

    public AirDataQueue(AirDataMessage airMessage) {
        this.airDataMessage = airMessage;
    }

    public static Channel connectToQueue() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        return channel;
    }

    public int putIntoQueue(String routingKey) throws Exception{
        Channel channel = connectToQueue();

        //TODO : Helper function to verify the routingkey syntax and compare it to the available list
        channel.exchangeDeclare(EXCHANGE_NAME,TOPIC,false,true,false,null);

        String message = (new Gson()).toJson(this.airDataMessage);
        channel.basicPublish(EXCHANGE_NAME,routingKey,null,message.getBytes("UTF-8"));

        return 0;
    }

    public static int disconnect(Connection connection) throws IOException {
        connection.close();
        return 0; //on success
    }
}
