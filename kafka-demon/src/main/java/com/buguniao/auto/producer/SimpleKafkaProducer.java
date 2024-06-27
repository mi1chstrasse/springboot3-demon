package com.buguniao.auto.producer;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author buguniao
 * 简单的 kafka 生产者
 */
@RestController
@Slf4j
public class SimpleKafkaProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    @GetMapping("/send/{message}")
    public void sendMessage(@PathVariable("message") String simpleMessage) {
        ProducerRecord<String, String> record = new ProducerRecord<String, String>("topic1", simpleMessage);
        kafkaTemplate.send("topic1",0,"tag",simpleMessage);
    }

    @GetMapping("/send2/{message}")
    public void sendMessage2(@PathVariable("message") String simpleMessage) {
        ProducerRecord<String, String> record = new ProducerRecord<String, String>("test", simpleMessage);
        kafkaTemplate.send("test",0,"tag",simpleMessage);
    }

    /**
     * 生产者确认
     * @param simpleMessage string
     */
    @GetMapping("/kafka/callbackOne/{message}")
    public void callbackOne(@PathVariable("message") String simpleMessage) {
        ProducerRecord<String, String> record = new ProducerRecord<>("topic1", simpleMessage);
        kafkaTemplate.send(record).whenComplete((r,throwable) -> {
            if (throwable != null) {
                log.error("发送消息失败:{}", throwable.getMessage());
            }
            String topic = r.getRecordMetadata().topic();
            // 消息发送到的分区
            int partition = r.getRecordMetadata().partition();
            // 消息在分区内的offset
            long offset = r.getRecordMetadata().offset();
            System.out.println("发送消息成功:" + topic + "-" + partition + "-" + offset);
        });
    }




}
