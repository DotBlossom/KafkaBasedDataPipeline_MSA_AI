package com.example.kafkaAsyncTest.service;


import com.example.kafkaAsyncTest.DTO.EventTransfer;
import com.example.kafkaAsyncTest.entity.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;


@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaMessagePublisher {

    //@Autowired
    //private KafkaTemplate<String, Object> exactlyOnceKafkaTemplate;

    private final KafkaTemplate<String, Object> exactlyOnceKafkaTemplate;
    private final KafkaTemplate<String, Object> defaultKafkaTemplate;

    // 나중에 topicname도 가져오거나 케이스 분기하자.
    @Value("${kafkaPipeline.topic-name-1}")
    String topicName = "";

    public <T> void sendObjectToTopic(EventTransfer res) {
        try {

            // 자료형을 나눌 key 필요, eventEntity



            String key = String.valueOf(res.getEventId() % 3);

            // alloc Key - obj Mapper
            ProducerRecord<String, Object> record = new ProducerRecord<>(topicName, key, res);

            CompletableFuture<SendResult<String, Object>> future =
                    exactlyOnceKafkaTemplate.send(record);

            // ACK BROKER COGNITIONS

            future.whenComplete((result,ex) -> {
                if (ex == null) {
                    RecordMetadata recordMetadata = result.getRecordMetadata();
                    sendLog(res.toString(), recordMetadata);

                } else {
                    System.out.println("Unable to send message=[" +
                            res.toString() + "] due to : " + ex.getMessage());
                }
            });

        } catch (Exception ex) {
            System.out.println("prod error has occured!" + res.getEventType());
        }
    }


    private static void sendLog(String message, RecordMetadata recordMetadata) {
        log.info("Received message = {} with offset = {}", message, recordMetadata.offset());
        log.info("Topic Name = {}", recordMetadata.topic());
        log.info("Topic Partition Count = {}", recordMetadata.partition());
    }

}
