package com.kafkaClusterContainer.kafkaClusterContainer.controller;


import com.kafkaClusterContainer.kafkaClusterContainer.entity.EventBlock;
import com.kafkaClusterContainer.kafkaClusterContainer.service.KafkaMessagePublisher;
import com.kafkaClusterContainer.kafkaClusterContainer.service.KafkaMessageSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cluster/test")
public class defaultRestAPIController {



    private final KafkaMessagePublisher kafkaMessagePublisher;
    private final KafkaMessageSubscriber kafkaMessageSubscriber;


    @PostMapping
    public ResponseEntity<EventBlock> createEventResponse(@RequestBody EventBlock eventReq) {


        kafkaMessagePublisher.sendObjectToTopic(res);

        return ResponseEntity.ok(res);
    }


}
