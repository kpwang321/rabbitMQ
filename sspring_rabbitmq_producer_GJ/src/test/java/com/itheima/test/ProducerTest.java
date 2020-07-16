package com.itheima.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
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

    /**
     * 过期时间
     *    1.队列统一过期
     *
     *    2.单个消息过期
     * 如果设置了消息的过期时间，也设置了队列的过期时间，以时间短的为准
     * 队列过期后，会将队列所有消息全部移除
     * 消息过期后，只有消息在队列顶端，才会判断其是否过期
     */
    @Test
    public void testTtl(){
        /*for (int i = 0; i <10 ; i++) {
            rabbitTemplate.convertAndSend("test_exchange_ttl","ttl.me",i+"ttl..........");

        }*/
        MessagePostProcessor messagePostProcessor=new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //1设置message的信息
                message.getMessageProperties().setExpiration("5000");//消息的过期时间
                //2返回该消息
                return message;
            }
        };
        //消息单独过期
        //rabbitTemplate.convertAndSend("test_exchange_ttl", "ttl.me","message ttl..........", messagePostProcessor);
        for (int i = 0; i <10 ; i++) {
            if (i==5){
                rabbitTemplate.convertAndSend("test_exchange_ttl", "ttl.me",i+"message ttl..........", messagePostProcessor);

            }else {
                rabbitTemplate.convertAndSend("test_exchange_ttl", "ttl.me",i+"message ttl..........");

            }

        }


    }

    /**
     * 发送测试死信消息
     *     1.过期时间
     *     2.长度限制
     *     3.消息拒收
     */
    @Test
    public void testDlx(){
        //1.测试过期时间
        //rabbitTemplate.convertAndSend("test_exchange_dlx","test.dlx.ssss","我是一条消息，我会成为死信吗");
        //2.长度限制
        /*for (int i = 0; i <20 ; i++) {
            rabbitTemplate.convertAndSend("test_exchange_dlx","test.dlx.ssss",i+"我是一条消息，我会成为死信吗");

        }*/
        //3.测试消费者拒收
        rabbitTemplate.convertAndSend("test_exchange_dlx","test.dlx.ssss","我是一条消息，我会成为死信吗");


    }
    @Test
    public void testDelay() throws InterruptedException {
        //1发送订单信息。将来是在订单系统中，下单成功后，发送消息
        rabbitTemplate.convertAndSend("order_exchange","order.msg","订单信息:id=1,time=2020/7/16 17:06:21");
        //2打印倒计时10秒
        for (int i = 10; i >0 ; i--) {
            System.out.println(i+"....");
            Thread.sleep(1000);
        }

    }

}
