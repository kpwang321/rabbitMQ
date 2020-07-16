package com.itheima.listener;


import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * @author kpwang
 * @create 2020-07-16 3:14
 */
@Component
public class DlxListener implements ChannelAwareMessageListener {
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            //1接收转换消息
            System.out.println(new String(message.getBody()));
            System.out.println(deliveryTag);
            //2处理业务逻辑
            System.out.println("处理业务逻辑");

            int i=1/0;

            //3手动签收
            channel.basicAck(deliveryTag,true);
        }catch (Exception e){
            Thread.sleep(1000);
            System.out.println("出现异常拒绝接收");
            //4拒绝签收 不回原队列
            //第三个参数requeue：重回队列。如果设置true，则消息重新回到queue，broker会重新发送该消息给消费端
            channel.basicNack(deliveryTag,true,false);
        }

    }
}
