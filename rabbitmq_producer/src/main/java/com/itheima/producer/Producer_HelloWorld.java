package com.itheima.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author kpwang
 * @create 2020-07-15 18:25
 * 发送消息
 */
public class Producer_HelloWorld {
    public static void main(String[] args) throws IOException, TimeoutException {
        //1.建立连接工厂
        ConnectionFactory factory=new ConnectionFactory();
        //2.设置参数
        factory.setHost("192.168.5.103");//ip  默认值为localhost
        factory.setPort(5672);//默认值 5672
        factory.setVirtualHost("/itcast");//虚拟机   默认值/
        factory.setUsername("heima");//用户名   默认值guest
        factory.setPassword("heima");//密码     默认值guest
        //3.创建连接 connection
        Connection connection = factory.newConnection();
        //4.创建channel
        Channel channel = connection.createChannel();
        //5.创建队列Queue
        //queueDeclare(String queue, boolean durable, boolean exclusive,boolean autoDelete, Map<String, Object> arguments)
       /**  queue:队列名称
        *   durable：是否持久化，当mq重启之后，数据还在
        *   exclusive：是否独占。只能有一个消费者监听这个队列。当connection关闭时，是否删除队列
        *   autoDelete：是否自动删除。当没有consumer时，自动删除
        *   arguments：参数。
        */
       //如果没有一个名字叫hello_world的队列，则会创建这个队列
        channel.queueDeclare("hello_world",true,false,false,null);
        //6.发送消息
        //basicPublish(String exchange, String routingKey, BasicProperties props, byte[] body)
        /**
         * exchange:交换机的名称，简单模式下交换机会使用默认值
         * routingKey：路由名称
         * props：配置信息
         * body：发送的消息数据
         * */
        String body="hello rabbitmq~~~~~";
        channel.basicPublish("","hello_world",null,body.getBytes());
        channel.close();
        connection.close();
    }
}
