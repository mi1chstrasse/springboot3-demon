package com.buguniao.test;

import com.buguniao.manunal.config.KafkaReceiverConfig;
import jakarta.annotation.Nonnull;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class KafkaReceiverConfigTest {

    @InjectMocks
    private KafkaReceiverConfig kafkaReceiverConfig;

    private RecordFilterStrategy<String, String> recordFilterStrategy;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ConcurrentKafkaListenerContainerFactory<String, String> factory = kafkaReceiverConfig.kafkaListenerAutoCommitFactory();
        recordFilterStrategy = new RecordFilterStrategy<String, String>() {
            @Override
            public boolean filter(@Nonnull ConsumerRecord<String, String> consumerRecord) {
                if (Optional.ofNullable(consumerRecord.value()).isPresent()) {
                    for (Header header : consumerRecord.headers()) {
                        if ("tag".equals(header.key()) && new String(header.value()).equals(new String("kafka".getBytes(StandardCharsets.UTF_8)))) {
                            return false;
                        }
                    }
                }
                return true;
            }
        };
    }

    @Test
    public void testRecordFilterStrategy() {
        ConsumerRecord<String, String> recordWithKafkaTag = new ConsumerRecord<>("topic1", 0, 0, "key", "value");
        recordWithKafkaTag.headers().add(new RecordHeader("tag", "kafka".getBytes(StandardCharsets.UTF_8)));

        ConsumerRecord<String, String> recordWithoutKafkaTag = new ConsumerRecord<>("topic1", 0, 0, "key", "value");
        recordWithoutKafkaTag.headers().add(new RecordHeader("tag", "other".getBytes(StandardCharsets.UTF_8)));

        assertFalse(recordFilterStrategy.filter(recordWithKafkaTag));
        assertTrue(recordFilterStrategy.filter(recordWithoutKafkaTag));
    }
}