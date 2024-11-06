package com.example.kafkaAsyncTest.service;

import com.example.kafkaAsyncTest.DTO.EventTransfer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventBlockServiceImpl implements EventBlockService {



    @Override
    public <T> EventTransfer<T> createEventTransfer(String eventType, String eventName, T data, int statusCode) {


        EventTransfer<T> evt = new EventTransfer<T>();

        evt.setEventType(eventType);
        evt.setEventName(eventName);
        evt.setStatusCode(statusCode);

        evt.setCreatedAt(LocalDateTime.now());
        evt.setData(data);
        return evt;

        //EventTransfer<Product> productEvent = CreateEventTransfer("T", "createProduct", 201);

    };




}
