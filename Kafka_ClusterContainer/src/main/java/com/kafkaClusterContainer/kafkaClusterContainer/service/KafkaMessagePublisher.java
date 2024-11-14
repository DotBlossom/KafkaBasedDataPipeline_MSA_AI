package com.kafkaClusterContainer.kafkaClusterContainer.service;


import com.kafkaClusterContainer.kafkaClusterContainer.DTO.EventTransfer;
import com.kafkaClusterContainer.kafkaClusterContainer.entity.EventBlock;
import com.kafkaClusterContainer.kafkaClusterContainer.entity.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;


@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaMessagePublisher {

    //@Autowired
    //private KafkaTemplate<String, Object> exactlyOnceKafkaTemplate;

    // 만약 Result 던지면, evt로 던지기 Result토픽에

    private final KafkaTemplate<String, Object> exactlyOnceKafkaTemplate;
    private final KafkaTemplate<String, Object> defaultKafkaTemplate;

    // 나중에 topicname도 가져오거나 케이스 분기하자.
    @Value("${kafkaPipeline.topic-name-3}")
    String topicName = "";

    // 각 MSA로 ㄱㄱ

    public void sendObjectToTopic(EventBlock evb) {
        try {
            String key = String.valueOf(evb.getResultId() % 3);
            EventTransfer evt = new EventTransfer();

            // propagation
            if (Objects.equals(evb.getEventType(), "MSA2")) {


            // alloc Key - obj Mapper
            // evb -> evt 로 변환 핋요
            // 순서에 따라 각 call id 할당
            evt.setQueryId(evb.getEventRelatedIds().get(evb.getCurrentDepth()));
            evt.setEventType("MSA2");

            } else if (evb.isEventEndFlag()) {

                // 보내야 할 경우는 다른곳에..



            }

            // 추후에 케이스 or End Event?
            ProducerRecord<String, Object> record = new ProducerRecord<>(topicName, key, evt);


            CompletableFuture<SendResult<String, Object>> future =
                    exactlyOnceKafkaTemplate.send(record);


            future.whenComplete((result,ex) -> {
                if (ex == null) {
                    RecordMetadata recordMetadata = result.getRecordMetadata();
                    sendLog(evt.toString(), recordMetadata);

                } else {
                    System.out.println("Unable to send message=[" +
                            evt.toString() + "] due to : " + ex.getMessage());
                }
            });

        } catch (Exception ex) {
            System.out.println("prod error has occured!" + evb.getTransactionIndicator());
        }
    }


    private static void sendLog(String message, RecordMetadata recordMetadata) {
        log.info("Received message = {} with offset = {}", message, recordMetadata.offset());
        log.info("Topic Name = {}", recordMetadata.topic());
        log.info("Topic Partition Count = {}", recordMetadata.partition());
    }

}
