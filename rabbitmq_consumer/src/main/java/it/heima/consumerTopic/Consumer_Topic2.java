package it.heima.consumerTopic;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author kpwang
 * @create 2020-07-15 18:47
 */
public class Consumer_Topic2 {
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

        String queue1Name="test_topic_queue1";
        String queue2Name="test_topic_queue2";

        //接收消息
        //basicConsume(String queue, boolean autoAck, Consumer callback)
        /**
         * queue :队列名称
         * autoAck: 是否自动确认
         * callback：回调对象
         * */
        Consumer consumer=new DefaultConsumer(channel){
            //回调方法，当收到消息后会自动执行该方法
            /**
             * consumerTag：标识
             * envelope：获取一些信息，交换机。路由key。。。。。
             * properties：配置信息
             * body：数据
             * */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("body:"+new String(body));
                System.out.println("将日志信息打印到控制台..");
            }
        };
        channel.basicConsume(queue2Name,true,consumer);
    }
}
