package com.itheima.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author kpwang
 * @create 2020-07-16 2:39
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-rabbitmq-producer.xml")
public class ProducerTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 1.确认模式开启：ConnectionFactory中开启 publisher-confirms="true"
     * 2.在rabbitTemplate定义ConfirmCallBack回调函数
     */
    @Test
    public void testConfirm(){
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             * @param correlationData 相关配置消息
             * @param ack  exchange交换机 是否成功收到消息 true 成功 false失败
             * @param cause 失败原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("confirm方法被执行了........");
                if (ack){
                    //接收成功
                    System.out.println("消息接收成功"+cause);
                }else {
                    //接收失败
                    System.out.println("消息接收失败"+cause);
                    //做一些处理，让消息再次发送
                }

            }
        });
        //发送消息
        rabbitTemplate.convertAndSend("test_exchange_confirm","confirm","confirm..........");

    }

    /**
     * 回退模式：当消息发送给Exchange后，Exchange路由到Queue失败  才会执行 ReturnCallback
     *   1.开启回退模式  publisher-returns="true"
     *   2.设置returnCallBack
     *   3.设置Exchange处理消息的模式
     *       1.如果消息没有路由到Queue，则丢弃消息(默认)
     *       2.如果消息没有路由到Queue，返回给消息发送方returnCallback
     */
    @Test
    public void testReturn(){
        //设置交换机处理失败消息的模式
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            /**
             * @param message 发送消息对象
             * @param replyCode  失败错误码
             * @param replyText  失败信息
             * @param exchange  交换机
             * @param routingKey  路由键
             */
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                System.out.println("return执行了。。。。。。。。。。");
                System.out.println(message);
                System.out.println(replyCode);
                System.out.println(replyText);
                System.out.println(exchange);
                System.out.println(routingKey);
                //处理
            }
        });
        //发送消息
        rabbitTemplate.convertAndSend("test_exchange_confirm","confirm11","confirm..........");

    }
    @Test
    public void testSend(){
        for (int i = 0; i <10 ; i++) {
            rabbitTemplate.convertAndSend("test_exchange_confirm","confirm",i+"confirm..........");

        }
        //发送消息

    }

}
