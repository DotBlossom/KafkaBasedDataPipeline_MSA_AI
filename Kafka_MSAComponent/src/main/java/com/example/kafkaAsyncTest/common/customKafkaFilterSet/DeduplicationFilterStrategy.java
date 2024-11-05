package com.example.kafkaAsyncTest.common.customKafkaFilterSet;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DeduplicationFilterStrategy implements RecordFilterStrategy<String,Object>  {

    // 추후 DB나 redis에서 스케쥴링해서 비우기를 구현하자.
    private final Map<String, Long> filterKeys = new ConcurrentHashMap<>();

    @Scheduled(fixedRate = 600000) // 10분마다 실행
    public void cleanup() {
        long cutoffTime = System.currentTimeMillis() - 600000;
        filterKeys.entrySet().removeIf(entry -> entry.getValue() < cutoffTime);
        System.out.println("keyProps discarded");
    }

    @Override
    public boolean filter(ConsumerRecord<String,Object>  consumerRecord) {
        String key = consumerRecord.key();
        if (filterKeys.containsKey(key)) {
            System.out.println("Duplicate message discarded: " + key);
            return false;
        } else {
            filterKeys.put(key, System.currentTimeMillis());
            return true;
        }
    }
}
