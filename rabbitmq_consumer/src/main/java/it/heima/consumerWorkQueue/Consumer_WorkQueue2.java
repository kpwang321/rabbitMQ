package it.heima.consumerWorkQueue;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author kpwang
 * @create 2020-07-15 18:47
 */
public class Consumer_WorkQueue2 {
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
        channel.queueDeclare("work_queues",true,false,false,null);

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
            }
        };
        channel.basicConsume("work_queues",true,consumer);
    }
}
