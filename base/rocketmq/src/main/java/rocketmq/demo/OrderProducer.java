package rocketmq.demo;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

/**
 * @author HuSen
 * @since 2020/7/16 11:30 上午
 */
public class OrderProducer {

    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("demo_producer_order_group");
        producer.setNamesrvAddr("106.52.162.34:9876");
        producer.start();
        // keys 消息的唯一键
        for (int i = 0; i < 5; i++) {
            Message message = new Message("Topic_Order_Demo", "Tags", "Keys_" + i, "hello!".getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult result = producer.send(message, (list, message1, o) -> {
                // 获取队列的对应下标
                Integer index = (Integer) o;
                return list.get(index);
            }, 0);
            System.out.println(result);
        }
    }
}
