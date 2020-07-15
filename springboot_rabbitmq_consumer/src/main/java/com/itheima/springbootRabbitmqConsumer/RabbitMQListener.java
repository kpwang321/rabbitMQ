package com.itheima.springbootRabbitmqConsumer;


import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author kpwang
 * @create 2020-07-16 1:25
 */
@Component
public class RabbitMQListener {
    @RabbitListener(queues = "boot_queue")
    public void listenerQueue(Message message){
        System.out.println(new String(message.getBody()));
    }
}
