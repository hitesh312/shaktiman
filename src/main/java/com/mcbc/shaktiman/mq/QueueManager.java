package com.mcbc.shaktiman.mq;

import com.mcbc.shaktiman.common.Constant;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

import javax.jms.*;
import java.util.HashMap;
import java.util.Map;

public class QueueManager {

    static Connection connection;
    static Session session;
    static Map<String, Topic> topics;

    private static Topic getTopic(String topic) {
        try {
            if (!topics.containsKey(topic)) topics.put(topic, session.createTopic(topic));
            return topics.get(topic);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Unable to create Topic");
        }
    }

    private static void startBroker() {
        try {
            BrokerService brokerService = new BrokerService();
            brokerService.setBrokerName(Constant.brokerName);
            brokerService.addConnector(Constant.brokerAddress);
            brokerService.start();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Unable to start broker");
        }
    }

    private static void createConnection() {
        try {
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(Constant.brokerConnectURL);

            // Create a Connection
            connection = connectionFactory.createConnection();
            connection.setClientID(Constant.clientID);

            // Create a Session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            //start
            connection.start();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Unable to connect to broker");
        }
    }

    public static void publish(String topic, String text) {
        try {
            Topic destination = getTopic(topic);

            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);

            TextMessage message = session.createTextMessage(text);
            producer.send(message);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Unable to connect to broker");
        }
    }

    private static void startActionQueueListener() {
        Thread t = new Thread(() -> {
            try {
                // Create the destination (Topic or Queue)
                Topic destination = getTopic(Constant.ACTION);

                // Create a MessageConsumer from the Session to the Topic or Queue
                MessageConsumer consumer = session.createDurableSubscriber(destination, Constant.clientID);

                while (true) {
                    // Wait for a message
                    Message message = consumer.receive();

                    if (message instanceof TextMessage) {
                        TextMessage textMessage = (TextMessage) message;
                        String text = textMessage.getText();
                        System.out.println("Received: " + text);
                    } else {
                        System.out.println("Received: " + message);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException("Unable to start consumer");
            }
        });
        t.start();
    }

    public static void start() {
        topics = new HashMap<>();
        startBroker();
        createConnection();
        startActionQueueListener();
    }
}
