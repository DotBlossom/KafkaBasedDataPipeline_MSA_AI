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
    public EventTransfer createEventTransfer(String eventType, String eventName, String statusCode) {


        EventTransfer evt = new EventTransfer();

        evt.setEventType(eventType);
        evt.setEventName(eventName);
        evt.setStatusCode(statusCode);

        evt.setCreatedAt(LocalDateTime.now());
        evt.setEventId(System.currentTimeMillis());

        return evt;

        //EventTransfer<Product> productEvent = CreateEventTransfer("T", "createProduct", 201);

    };




}
