package rocketmq.demo;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.*;

/**
 * @author HuSen
 * @since 2020/7/16 1:52 下午
 */
public class TransactionProducer {

    private static final ConcurrentHashMap<String, LocalTransactionState> TRANSACTION_STATES = new ConcurrentHashMap<>();

    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException {
        TransactionMQProducer producer = new TransactionMQProducer("demo_producer_transaction_group");
        producer.setNamesrvAddr("118.24.38.46:9876");
        producer.setTransactionListener(new TransactionListener() {

            @Override
            public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
                System.out.println("本地事务------");
                // 本地事务
                String transactionId = msg.getTransactionId();
                // 业务执行
                System.out.println("hello demo transaction:" + transactionId);
                TRANSACTION_STATES.put(transactionId, LocalTransactionState.COMMIT_MESSAGE);
                return LocalTransactionState.UNKNOW;
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) {

                // 消息回查
                String transactionId = msg.getTransactionId();
                System.out.println("消息回查------" + transactionId);
                if (TRANSACTION_STATES.containsKey(transactionId)) {
                    return TRANSACTION_STATES.get(transactionId);
                }
                return LocalTransactionState.UNKNOW;
            }
        });

        ExecutorService executorService = new ThreadPoolExecutor(
                2, 5, 100, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2000), r -> new Thread(r, "client-transaction-msg-check-thread"));

        producer.setExecutorService(executorService);

        producer.start();

        Message message = new Message("Topic_Transaction_Demo", "Tags", "Keys_1", "hello!".getBytes(RemotingHelper.DEFAULT_CHARSET));

        TransactionSendResult result = producer.sendMessageInTransaction(message, "hello-transaction");
        System.out.println(result);

//        producer.shutdown();
    }
}
