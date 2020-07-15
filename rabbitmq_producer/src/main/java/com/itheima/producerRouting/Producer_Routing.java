package com.itheima.producerRouting;

import com.rabbitmq.client.BuiltinExchangeType;
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
public class Producer_Routing {
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
        //5.创建交换机
        //exchangeDeclare(String exchange, BuiltinExchangeType type,
        //                boolean durable, boolean autoDelete,
        //                boolean internal, Map<String, Object> arguments)
        /**
         * exchange:交换机名称
         * type:交换机的类型
         *      DIRECT("direct"),定向
         *      FANOUT("fanout"),广播，发送消息到每一个与之绑定的队列
         *      TOPIC("topic"),通配符
         *      HEADERS("headers");参数匹配
         * durable：是否持久化
         * autoDelete：是否自动删除
         * internal：内部使用，一般时false
         * arguments：参数
         * */
        String exchangeName="test_dirent";
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT,true,false,false,null);
        //6.创建队列
        String queue1Name="test_dirent_queue1";
        String queue2Name="test_dirent_queue2";

        channel.queueDeclare(queue1Name,true,false,false,null);
        channel.queueDeclare(queue2Name,true,false,false,null);
        //7.绑定队列和交换机
        //queueBind(String queue, String exchange, String routingKey)
        /**
         * queue  队列名称
         * exchange  交换机名称
         * routingKey  路由键，绑定规则
         *     如果交换机的类型为fanout，routingKey设置为空
         * */
        //队列1的绑定
        channel.queueBind(queue1Name,exchangeName,"error");
        //队列2的绑定
        channel.queueBind(queue2Name,exchangeName,"info");
        channel.queueBind(queue2Name,exchangeName,"error");
        channel.queueBind(queue2Name,exchangeName,"warning");

        //8.发送消息
        String body="日志信息：张三调用了findAll方法。。。日志级别：info。。。。";
        channel.basicPublish(exchangeName,"info",null,body.getBytes());
        String body1="日志信息：张三调用了delete方法。。。日志级别：error。。。。";
        channel.basicPublish(exchangeName,"error",null,body1.getBytes());
        //9.释放资源
        channel.close();
        connection.close();

    }
}
