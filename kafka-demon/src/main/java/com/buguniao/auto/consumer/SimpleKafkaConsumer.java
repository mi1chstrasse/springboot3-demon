package com.buguniao.auto.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author buguniao
 * 简单消费者
 */
@Component
public class SimpleKafkaConsumer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * @param message 消息记录
     */
    @KafkaListener(topics = {"topic1"})
    public void onMessage1(ConsumerRecord<String, String> message) {
        // 消费的哪个topic、partition的消息,打印出消息内容
        System.out.println("消费者 1 简单消费：" + message.topic() + "-" + message.partition() + "-" + message.value());
    }


    /**
     * 指定多个主题， 并且指定线程数
     *
     * @param consumerRecord 消息记录
     */
    @KafkaListener(topics = {"topic1", "test"}, concurrency = "3")
    public void consumer(ConsumerRecord<?, String> consumerRecord) {
        // 消费的哪个topic、partition的消息,打印出消息内容
        System.out.println("消费者 2 简单消费：" + consumerRecord.topic() + "-" + consumerRecord.partition() + "-" + consumerRecord.value());
    }


    /**
     * @return void
     * topics和topicPartitions不能同时使用；
     * @Title 指定topic、partition、offset消费
     * @Description 同时监听topic1和test，监听topic1的0号分区、test的 "0号和1号" 分区，指向1号分区的offset初始值为8
     * @Param [record]
     **/
    @KafkaListener(groupId = "test-group", topicPartitions = {
            @TopicPartition(topic = "topic1" ,partitions = "40")
    })
    public void onMessage2(ConsumerRecord<?, ?> record) {
        System.out.println("topic:" + record.topic() + "|partition:" + record.partition() + "|offset:" + record.offset() + "|value:" + record.value());
    }


    /**
     * 批量消费
     * @param records
     */
    @KafkaListener(groupId = "test-group", topics = "test")
    public void onMessage3(List<ConsumerRecord<?, String>> records) {
        System.out.println(">>>批量消费一次，records.size()=" + records.size());
        for (ConsumerRecord<?, ?> record : records) {
            System.out.println(record.value());
        }
    }

}
