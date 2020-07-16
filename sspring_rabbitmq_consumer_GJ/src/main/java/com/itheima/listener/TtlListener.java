package com.itheima.listener;


import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * @author kpwang
 * @create 2020-07-16 3:14
 * consumer 限流
 *     1.确保Ack机制为手动确认
 *     2.配置属性prefetch="1",表示消费端每次从mq拉取一条消息来消费，直到手动确认消息完毕后，才会继续拉取下一条消息
 */
@Component
public class TtlListener implements ChannelAwareMessageListener {
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        //Thread.sleep(1000);
        System.out.println(new String(message.getBody()));
    }
}
