package rocketmq.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;

/**
 * @author HuSen
 * @since 2020/7/16 1:15 下午
 */
@Slf4j
public class OrderConsumer {

    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("demo_consumer_order_group");
        consumer.setNamesrvAddr("106.52.162.34:9876");
        consumer.subscribe("Topic_Order_Demo", "*");
        consumer.setConsumeMessageBatchMaxSize(2);
        consumer.setMessageListener((MessageListenerOrderly) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                String topic = msg.getTopic();
                String tags = msg.getTags();
                String keys = msg.getKeys();
                byte[] body = msg.getBody();
                try {
                    log.info("接受到消息topic:{}, tags:{}, keys:{}, body:{}", topic, tags, keys, new String(body, RemotingHelper.DEFAULT_CHARSET));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    // 消息重试
                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }
            }
            return ConsumeOrderlyStatus.SUCCESS;
        });
        consumer.start();
    }
}
