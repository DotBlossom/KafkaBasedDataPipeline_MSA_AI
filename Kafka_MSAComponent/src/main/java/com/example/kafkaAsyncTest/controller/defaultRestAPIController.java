package com.example.kafkaAsyncTest.controller;

import com.example.kafkaAsyncTest.entity.Result;
import com.example.kafkaAsyncTest.service.KafkaMessagePublisher;
import com.example.kafkaAsyncTest.service.KafkaMessageSubscriber;
import com.example.kafkaAsyncTest.service.defaultService.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class defaultRestAPIController {

    private final ResultService resultService;

    private final KafkaMessagePublisher kafkaMessagePublisher;
    private final KafkaMessageSubscriber kafkaMessageSubscriber;


    @PostMapping
    public ResponseEntity<Result> createResponse(@RequestBody Result resReq) {

        Result res = resultService.createResult(resReq);

        kafkaMessagePublisher.sendObjectToTopic(res);

        return ResponseEntity.ok(res);
    }

}
