package com.example.kafkaAsyncTest.controller;

import com.example.kafkaAsyncTest.common.listenerFunctionSet.ResultFunctionSet;
import com.example.kafkaAsyncTest.entity.Result;
import com.example.kafkaAsyncTest.service.KafkaMessagePublisher;
import com.example.kafkaAsyncTest.service.KafkaMessageSubscriber;
import com.example.kafkaAsyncTest.service.defaultService.ResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
@Slf4j
public class defaultRestAPIController {

    private final ResultService resultService;

    private final KafkaMessagePublisher kafkaMessagePublisher;
    private final KafkaMessageSubscriber kafkaMessageSubscriber;
    private final ResultFunctionSet resultFunctionSet;

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // ************** Common Method EOL

    @PostMapping
    public ResponseEntity<Result> createResponse(@RequestBody Result resReq) {

        Result res = resultService.createResult(resReq);

        //String temp = resultFunctionSet.resultStage_1("a");
        //res.setEventType(temp);


        //kafkaMessagePublisher.sendObjectToTopic(res);

        return ResponseEntity.ok(res);
    }

    @PostMapping("/async")
    public ResponseEntity<String> exportPropagation(@RequestBody Result res) {

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            // 비동기 함수 1 실행
            System.out.println("async 1...");
            sleep(1000); // 1초 대기
            return "결과 1";
        });

        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            // 비동기 함수 2 실행
            System.out.println("async 2 ...");
            sleep(2000); // 2초 대기
            return 2;
        });

        CompletableFuture<Boolean> future3 = CompletableFuture.supplyAsync(() -> {
            // 비동기 함수 3 실행
            System.out.println("async 3 ...");
            sleep(1500); // 1.5초 대기
            return true;
        });

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(future1, future2, future3);

        CompletableFuture<String> responseEntityFuture = allFutures.thenApply(v -> {
            try {
                String result1 = future1.get();
                Integer result2 = future2.get();
                Boolean result3 = future3.get();

                System.out.println("res 1 결과: " + result1);
                System.out.println("res 2 결과: " + result2);
                System.out.println("res 3 결과: " + result3);

                return result1; // result1을 ResponseEntity에 담아 반환
            } catch (InterruptedException | ExecutionException e) {

                return "codeError";
            }
        });


        String prov = responseEntityFuture.join();
        return ResponseEntity.ok(prov);
    }

}
