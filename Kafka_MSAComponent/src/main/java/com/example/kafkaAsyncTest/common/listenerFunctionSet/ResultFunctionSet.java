package com.example.kafkaAsyncTest.common.listenerFunctionSet;

import org.springframework.stereotype.Service;

@Service
public class ResultFunctionSet {

    // type code : transaction 단위 분기. 2개로 예씨하자.
    // actual functioms, if block[i] == 10
    public String resultStage_1(String message) {
        // 메시지 처리 로직 구현
        // ...
        return "Processed1: " + message;
    }

    // actual functioms, if block[i] == 11
    public String resultStage_2(String message) {
        // 메시지 처리 로직 구현
        // ...
        return "Processed2: " + message;
    }


}
