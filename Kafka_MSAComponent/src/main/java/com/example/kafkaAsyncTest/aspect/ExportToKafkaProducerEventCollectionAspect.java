package com.example.kafkaAsyncTest.aspect;


import com.example.kafkaAsyncTest.DTO.EventTransfer;
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
                // result -> eventBlocker
                // 메서드 메모한 type 긁어와서 분기하기.
                //String eventType, Stringe eventName, T data, int statusCode)

                String eventName = joinPoint.getSignature().getName();
                String eventType = resultInstance.getEventType();


                EventTransfer res = eventBlockService.createEventTransfer(eventType, eventName,  "200");
                kafkaMessagePublisher.sendObjectToTopic(res);

            }



        } else {
            // ErrorObject or EventBlock?
            EventTransfer res = eventBlockService.createEventTransfer("error", "error", "404");
            kafkaMessagePublisher.sendObjectToTopic(res);
        }

    }


}
