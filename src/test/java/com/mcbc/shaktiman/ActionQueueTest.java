package com.mcbc.shaktiman;

import com.mcbc.shaktiman.common.Constant;
import com.mcbc.shaktiman.mq.QueueManager;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.jms.*;
import java.util.Scanner;

public class ActionQueueTest {

    @BeforeClass
    public static void setup() throws JMSException {
        ServerManager.main(null);
    }

    @Test
    public void sendMessage() throws JMSException {
        QueueManager.publish(Constant.ACTION,"hello");
    }
}
