package com.kafkaClusterContainer.kafkaClusterContainer.service.eventControlService;

import com.kafkaClusterContainer.kafkaClusterContainer.DTO.EventTransfer;
import com.kafkaClusterContainer.kafkaClusterContainer.service.KafkaMessagePublisher;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventPropagationService {

    // push Msg and (Others)If Complete -> Result 토픽에서 관리.
    // 만약 오류라면, 어디서 오류났는지 단계 블럭을 통해 관리해주자.
    // 그건 여기서 Transaction DB

    // 어디로 보내야 할지가 필요함.
    // eventBlock이 존재한다면? -> Transactional 블록이나 상황 해석후 라우팅..?
    // test니까, 일반적인 하나의 값만 ㄱㄱ





}
