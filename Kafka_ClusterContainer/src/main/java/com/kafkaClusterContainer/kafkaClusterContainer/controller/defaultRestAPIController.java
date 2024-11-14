package com.kafkaClusterContainer.kafkaClusterContainer.controller;


import com.kafkaClusterContainer.kafkaClusterContainer.DTO.EventTransfer;
import com.kafkaClusterContainer.kafkaClusterContainer.entity.EventBlock;
import com.kafkaClusterContainer.kafkaClusterContainer.service.KafkaMessagePublisher;
import com.kafkaClusterContainer.kafkaClusterContainer.service.KafkaMessageSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cluster/test")
public class defaultRestAPIController {



    private final KafkaMessagePublisher kafkaMessagePublisher;
    private final KafkaMessageSubscriber kafkaMessageSubscriber;


    @GetMapping("/transaction/external/user")
    public CompletableFuture<String> processPropagationEvent(EventTransfer evt) {

        //evt functions -> eb
        EventBlock evb;

        // send Msg for trigger something
        kafkaMessagePublisher.sendObjectToTopic(evb);


        // Datacontroller의 내부 리스너에 결과 메세지가 도착하기를 기다림.
        // 일단 통신이 시작되고부터 할당되는 메서드니 그렇게 대기가 길지않음 (max 5000ms)

        CompletableFuture<String> resultFuture = kafkaMessageSubscriber.listen();
        // 그럼 내부

        // 리스너에 특정 반응이 리턴되었다.




        // Case Not만약 또 부른다면? or 여러개 부른다면 supplyResultAppender 코드
        // msa 2, mas3 도 호출, 순서가 아니면 Parallel




        // Case if 만약 여기서 Transaction이 온전히 종료된다면? -> Return 200OK,



        // 만약 여기서 event propagation이 종료된다면? End Tag를 붙여서 관리.
        return resultFuture.thenApplyAsync(result -> {
            // 결과 가공 또는 추가 처리 로직
            return "Controller processed: " + result;
}
