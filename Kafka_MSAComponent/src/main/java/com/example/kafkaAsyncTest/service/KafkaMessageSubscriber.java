package com.example.kafkaAsyncTest.service;

import com.example.kafkaAsyncTest.common.customKafkaFilterSet.DeduplicationFilterStrategy;
import com.example.kafkaAsyncTest.entity.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaMessageSubscriber {

    @KafkaListener(topics = "${kafkaPipeline.topic-name-1}", containerFactory = "exactlyOnceKafkaListenerContainerFactory",
            groupId = "${kafkaPipeline.group-id-4}")
    public void listen(ConsumerRecord<String, Object> record) {
        //String topic = record.topic();

        Object value = record.value();
        Result res = (Result) value;

        sendLog(res.toString(), record);



    }

    @KafkaListener(topics = "${kafkaPipeline.topic-name-3}", containerFactory = "exactlyOnceKafkaListenerContainerFactory",
            groupId = "${kafkaPipeline.group-id-4}")
    public void listenResult(ConsumerRecord<String, Object> record) {

        Object value = record.value();
        Result res = (Result) value;

        sendLog(res.toString(), record);

        // 만약 result가 적당히 오면, (primaryMSA)
    }


    private static void sendLog(String message, ConsumerRecord<String, Object> record) {
        log.info("Receive message = {} with offset = {}", message, record.offset());
        log.info("Topic Name = {}", record.topic());
        log.info("Topic Partition Count = {}", record.partition());

    }
}
