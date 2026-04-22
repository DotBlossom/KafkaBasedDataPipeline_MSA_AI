package com.example.kafkaAsyncTest.controller;

import com.example.kafkaAsyncTest.common.customKafkaFilterSet.RequestUtils;
import com.example.kafkaAsyncTest.entity.Result;
import com.example.kafkaAsyncTest.service.KafkaMessagePublisher;
import com.example.kafkaAsyncTest.service.KafkaMessageSubscriber;
import com.example.kafkaAsyncTest.service.defaultService.ResultService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/test/v2")
public class deferredRestAPIController {

    private final ResultService resultService;

    private final KafkaMessagePublisher kafkaMessagePublisher;
    private final KafkaMessageSubscriber kafkaMessageSubscriber;

    private static final Executor CACHED_THREAD_POOL = Executors.newCachedThreadPool();


    @PostMapping
    public DeferredResult<ResponseEntity<Result>> deferredCreateResult(@RequestBody Result resReq) throws ExecutionException, InterruptedException {
        DeferredResult<ResponseEntity<Result>> deferredResult = new DeferredResult<>();

        log.info("in controller {} {}", RequestUtils.getRequest().getRequestURI(), Thread.currentThread());


        Result res = resultService.createResult(resReq);

        // 2. 비동기 작업 체이닝
        CompletableFuture.runAsync(() -> {
            log.info("비동기 작업 시작: {}", Thread.currentThread().getName());
            // kafka 전송 등 외부 MSA 통신 로직...
        }, CACHED_THREAD_POOL).thenAccept(v -> {
            // 3. 작업이 완료되면 그때 결과를 세팅 (이때 서블릿 쓰레드는 이미 반환된 상태)
            deferredResult.setResult(ResponseEntity.ok(res));
        }).exceptionally(ex -> {
            deferredResult.setErrorResult(ResponseEntity.status(500).build());
            return null;
        });
    
        // 4. 즉시 리턴 (서블릿 쓰레드 해방)
        return deferredResult;
        }


}
