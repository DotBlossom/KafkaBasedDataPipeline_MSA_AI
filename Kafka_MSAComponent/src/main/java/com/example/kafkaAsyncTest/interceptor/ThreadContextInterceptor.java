package com.example.kafkaAsyncTest.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.DeferredResultProcessingInterceptor;


@Component
public class ThreadContextInterceptor implements DeferredResultProcessingInterceptor {

    @Override
    public <T> void preProcess(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {
        // 현재 Security Context 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        deferredResult.onCompletion(() -> {
            try {
                // DeferredResult 완료 후, 새로운 스레드에서 Security Context 설정
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);
                SecurityContextHolder.setContext(securityContext);
            } finally {
                SecurityContextHolder.clearContext(); // Security Context 초기화
            }
        });
    }
}


    // ... 다른 메서드는 필요에 따라 구현 ...

    // ... 다른 메서드 구현 ...
