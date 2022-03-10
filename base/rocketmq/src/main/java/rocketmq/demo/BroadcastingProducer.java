package rocketmq.demo;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HuSen
 * @since 2020/7/16 11:30 上午
 */
public class BroadcastingProducer {

    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("demo_producer_broadcasting_group");
        producer.setNamesrvAddr("118.24.38.46:9876");
        producer.start();
        // keys 消息的唯一键
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Message message = new Message("Topic_Broadcasting_Demo", "Tags", "Keys_" + i, "hello!".getBytes(RemotingHelper.DEFAULT_CHARSET));
            messages.add(message);
        }
        SendResult result = producer.send(messages);
        System.out.println(result);
    }
}
