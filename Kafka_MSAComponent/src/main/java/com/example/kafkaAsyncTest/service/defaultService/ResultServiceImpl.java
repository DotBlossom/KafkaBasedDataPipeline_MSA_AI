package com.example.kafkaAsyncTest.service.defaultService;

import com.example.kafkaAsyncTest.entity.Result;
import com.example.kafkaAsyncTest.repository.ResultRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final ResultRepository resultRepository;

    @Override
    public Result createResult(Result resReq) {
        Result res = Result.builder()
                .eventType(resReq.getEventType())
                .eventRelatedIds(resReq.getEventRelatedIds())
                .currentEventLevel(resReq.getCurrentEventLevel())
                .transactionalEventLevel(resReq.getTransactionalEventLevel())
                .build();

        return resultRepository.save(res);
    }

}
