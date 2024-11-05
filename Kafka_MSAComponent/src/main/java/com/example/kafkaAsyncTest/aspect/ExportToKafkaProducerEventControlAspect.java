package com.example.kafkaAsyncTest.aspect;


import com.example.kafkaAsyncTest.service.KafkaMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class ExportToKafkaProducerEventControlAspect {


    private final KafkaMessagePublisher kafkaMessagePublisher;
    // producer Service Call - final

    // pointcut

    // typeDef

}
