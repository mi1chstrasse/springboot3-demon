package com.buguniao.test;

import com.buguniao.manunal.consumer.ManualConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.support.Acknowledgment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ManualConsumerTest {

    @InjectMocks
    private ManualConsumer manualConsumer;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private ConsumerAwareListenerErrorHandler errorHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // 模拟错误处理器
        when(errorHandler.handleError(any(), any(), any())).thenAnswer(invocation -> invocation.getArguments()[0]);
    }

    @Test
    public void testConsumer() {
        ConsumerRecord<?, String> consumerRecord = new ConsumerRecord<>("topic1", 0, 0, null, "value");
        Acknowledgment ack = mock(Acknowledgment.class);

        manualConsumer.consumeManual(consumerRecord, ack);

        verify(ack).acknowledge();
    }


    @Test
    public void testConsumerErrorHandler() {
        ConsumerRecord<?, String> consumerRecord = new ConsumerRecord<>("topic1", 0, 0, null, "value");
        Acknowledgment ack = mock(Acknowledgment.class);

        manualConsumer.consumeAuto(consumerRecord);

    }
}