package com.itheima;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author kpwang
 * @create 2020-07-15 23:55
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-rabbitmq-producer.xml")
public class ProducerTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testHelloWorld(){
        rabbitTemplate.convertAndSend("spring_queue","hello world spring rabbitmq");
    }
    //发送fanout消息
    @Test
    public void testFanout(){
        rabbitTemplate.convertAndSend("spring_fanout_exchage","","spring fanout......");
    }
    //发送topic消息
    @Test
    public void testTopics(){
         rabbitTemplate.convertAndSend("spring_topic_exchange","heima.hehe","spring topic");
         rabbitTemplate.convertAndSend("spring_topic_exchange","heima.hehe","spring topic");
         rabbitTemplate.convertAndSend("spring_topic_exchange","itcast.hehe","spring topic");
    }

    //发送direct消息
    @Test
    public void testDirect(){
         rabbitTemplate.convertAndSend("spring_topic_exchange","heima.hehe","spring topic");
         rabbitTemplate.convertAndSend("spring_direct_exchange","abcd","spring dirent2");
         rabbitTemplate.convertAndSend("spring_direct_exchange","abc","spring dirent1");
    }
}
