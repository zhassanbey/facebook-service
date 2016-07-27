/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.alem.semantics.facebook.service.leaderank.queue;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.GetResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeoutException;
import kz.alem.semantics.facebook.service.leaderank.model.UserNode;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author Zhasan
 */
@Service
public class RabbitQueue implements Serializable {

    private static final Logger LOG = Logger.getLogger(RabbitQueue.class);
    
    String name;
    int size;
    String host;
    String routingKey;
    String exchangeName;

    Connection connection;
    Connection inputConnection;
    ConnectionFactory factory;
    Channel channel;
    Channel inputChannel;
    Consumer consumer;

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
    public Channel getChannel(){
        return inputChannel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public RabbitQueue() {

    }

    public RabbitQueue(String name, String host, int port, String username, String pass, String exchangeName) throws IOException, TimeoutException {
        LOG.info("Right constructor");
        this.routingKey = "routingKeyLeaderank";
        this.name = name;
        this.host = host;
        this.exchangeName=exchangeName;
        factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(pass);
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.exchangeDeclare(exchangeName, "direct", true);
        channel.queueDeclare(name, true, false, false, null);
        channel.queueBind(name, exchangeName, routingKey);
        initInputChannel();
    }

    @Deprecated
    public RabbitQueue(String name, String host, int port) throws IOException, TimeoutException {
        this.name = name;
        this.host = host;
        factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername("zhassan");
        factory.setPassword("pre5pro");
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(name, true, false, false, null);
    }

    public void initInputChannel() throws IOException, TimeoutException {
//        inputConnection = factory.newConnection();
        inputChannel = connection.createChannel();
        inputChannel.exchangeDeclare(exchangeName, "direct", true);
        inputChannel.queueDeclare(name, true, false, false, null);
        inputChannel.queueBind(name, exchangeName, routingKey);
    }

    @Deprecated
    public RabbitQueue(String name, String host) throws IOException, TimeoutException {
        this.name = name;
        this.host = host;
        factory = new ConnectionFactory();
        factory.setHost(host);
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(name, true, false, false, null);
    }

    private void init() {

    }

    public void add(UserNode message) throws IOException, TimeoutException {
        byte[] arr = message.toByteArray();
        channel.basicPublish(exchangeName, routingKey, null, arr);
        LOG.info("added " + message + " to " + name);
        size++;
    }

    public UserNode remove() throws IOException, InterruptedException, TimeoutException, ClassNotFoundException {
        GetResponse response = inputChannel.basicGet(name, false);
        if (response != null) {
            long deliveryTag = response.getEnvelope().getDeliveryTag();
            inputChannel.basicAck(deliveryTag, false);
            UserNode node = UserNode.fromBytes(response.getBody());
            return node;
        }
        return null;
    }

    public boolean empty() throws IOException, InterruptedException, TimeoutException, ClassNotFoundException {
        GetResponse response = channel.basicGet(name, false);
        if (response != null) {
            channel.basicNack(response.getEnvelope().getDeliveryTag(), false, true);
            return false;
        }
        return true;
    }
}
