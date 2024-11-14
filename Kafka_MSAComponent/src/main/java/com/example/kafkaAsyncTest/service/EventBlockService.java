package com.example.kafkaAsyncTest.service;

import com.example.kafkaAsyncTest.DTO.EventTransfer;

public interface EventBlockService {
    public EventTransfer createEventTransfer(String eventType, String eventName, String statusCode);
}
