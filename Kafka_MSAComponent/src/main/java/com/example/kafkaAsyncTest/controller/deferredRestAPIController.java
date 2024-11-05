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

        CompletableFuture<HttpServletRequest> sf = CompletableFuture.supplyAsync(() -> {
            log.info("in completable future {}", RequestUtils.getRequest().getRequestURI());

            kafkaMessagePublisher.sendObjectToTopic(res);

            return RequestUtils.getRequest();

        }, CACHED_THREAD_POOL);

        HttpServletRequest req2 = sf.get();
        log.info("{}", req2);


        deferredResult.setResult(ResponseEntity.ok(res));

        return deferredResult;
    }


}
