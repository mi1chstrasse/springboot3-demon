package com.buguniao.manunal.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

/**
 * Kafka Consumer
 * @author buguniao
 */
@Slf4j
@Component
public class ManualConsumer {



    @KafkaListener(topics = {"topic1"}, containerFactory = "kafkaManualAckListenerContainerFactory" ,errorHandler = "consumerAwareErrorHandler")
    public void consumeManual(ConsumerRecord<?, String> consumerRecord, Acknowledgment ack) {
        log.info("手动提交: topic={}, partition={}, offset={}, key={}, value={}",
                consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset(),
                consumerRecord.key(), consumerRecord.value());
        ack.acknowledge();
    }

    @KafkaListener(topics = {"test"}, containerFactory = "kafkaListenerAutoCommitFactory" ,errorHandler = "consumerAwareErrorHandler")
    public void consumeAuto(ConsumerRecord<?, String> consumerRecord) {
        try {
            log.info("自动提交: topic={}, partition={}, offset={}, key={}, value={}",
                    consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset(),
                    consumerRecord.key(), consumerRecord.value());
            throw new RuntimeException("fail");
        } catch (Exception e) {
            log.error("自动提交失败: topic={}, partition={}, offset={}",
                    consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset(), e);
        }
    }
}