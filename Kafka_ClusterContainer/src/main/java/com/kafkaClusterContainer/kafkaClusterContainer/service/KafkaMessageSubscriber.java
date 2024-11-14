package com.kafkaClusterContainer.kafkaClusterContainer.service;




import com.kafkaClusterContainer.kafkaClusterContainer.DTO.EventTransfer;
import com.kafkaClusterContainer.kafkaClusterContainer.entity.EventBlock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaMessageSubscriber {

    private final KafkaMessagePublisher kafkaMessagePublisher;

    @KafkaListener(topics = "${kafkaPipeline.topic-name-1}", containerFactory = "exactlyOnceKafkaListenerContainerFactory",
            groupId = "${kafkaPipeline.group-id-4}")
    public void listen(ConsumerRecord<String, Object> record) {
        //String topic = record.topic();

        Object value = record.value();
        EventTransfer evt = (EventTransfer) value;


        // prev , curr 로 level 보고 판단해도됨..
        // 일단 test니까,

        if (evt.isCreatedEventBlock()) {

        } else {


        }


        sendLog(evt.toString(), record);




    }



    private static void sendLog(String message, ConsumerRecord<String, Object> record) {
        log.info("Receive message = {} with offset = {}", message, record.offset());
        log.info("Topic Name = {}", record.topic());
        log.info("Topic Partition Count = {}", record.partition());

    }

    private void sendPropagationTo(String transactionIndicator, EventBlock evb) {

        kafkaMessagePublisher.sendObjectToTopic(evb);
    }
}
