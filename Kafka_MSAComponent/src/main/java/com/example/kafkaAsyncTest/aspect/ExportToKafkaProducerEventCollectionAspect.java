package com.example.kafkaAsyncTest.aspect;


import com.example.kafkaAsyncTest.entity.Result;
import com.example.kafkaAsyncTest.service.EventBlockServiceImpl;
import com.example.kafkaAsyncTest.service.KafkaMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class ExportToKafkaProducerEventCollectionAspect {


    private final KafkaMessagePublisher kafkaMessagePublisher;
    private final EventBlockServiceImpl eventBlockService;
    // producer Service Call - final

    @AfterReturning(pointcut = "execution(* com.example.kafkaAsyncTest.controller.*.createResponse(..))",
    returning = "result")
    public void afterReturningExportToKafkaProducerAspect(JoinPoint joinPoint, Object result) {
        if (!(result instanceof Exception)) {

            if (result instanceof Result resultInstance) {

                kafkaMessagePublisher.sendObjectToTopic(eventBlockService.createEventTransfer(parms..));

            }



        } else {
            // ErrorObject or EventBlock?
            eventBlockService.createEventTransfer()
            kafkaMessagePublisher.sendObjectToTopic();
        }

    }


}
