package rocketmq.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;

/**
 * @author HuSen
 * @since 2020/7/16 1:15 下午
 */
@Slf4j
public class BroadcastingConsumerA {

    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("demo_consumer_broadcasting_group");
        consumer.setNamesrvAddr("118.24.38.46:9876");
        consumer.subscribe("Topic_Broadcasting_Demo", "*");
        // 默认是集群消费模式 同一个组一个消息只能被一个消费者获取 设置为广播模式
        consumer.setMessageModel(MessageModel.BROADCASTING);
        consumer.setConsumeMessageBatchMaxSize(10);
        consumer.setMessageListener((MessageListenerConcurrently) (list, consumeConcurrentlyContext) -> {
            for (MessageExt msg : list) {
                String topic = msg.getTopic();
                String tags = msg.getTags();
                byte[] body = msg.getBody();
                try {
                    log.info("接受到消息topic:{}, tags:{}, keys:{}, body:{}", topic, tags, msg.getKeys(), new String(body, RemotingHelper.DEFAULT_CHARSET));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    // 消息重试
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
    }
}
