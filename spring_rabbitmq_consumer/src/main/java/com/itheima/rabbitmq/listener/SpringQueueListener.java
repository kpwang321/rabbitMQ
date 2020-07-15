package com.itheima.rabbitmq.listener;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * @author kpwang
 * @create 2020-07-16 0:10
 */
public class SpringQueueListener implements MessageListener{
    @Override
    public void onMessage(Message message) {
        System.out.println(new String(message.getBody()));
    }


}
