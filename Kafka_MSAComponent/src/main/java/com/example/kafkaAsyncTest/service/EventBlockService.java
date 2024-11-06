package com.example.kafkaAsyncTest.service;

import com.example.kafkaAsyncTest.DTO.EventTransfer;

public interface EventBlockService {
    public <T> EventTransfer<T> createEventTransfer(String eventType, String eventName, T data, int statusCode);
}
