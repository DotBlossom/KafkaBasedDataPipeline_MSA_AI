package com.kafkaClusterContainer.kafkaClusterContainer.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
public class EventTransfer<T> {

    // if T : transaction , SeqT: seq+T , A : alarm , D : dataTransfer
    public String eventType;

    //AOP에서 메서드명 추출. -> eventName에 맞는 분기점 Controller에서 준비.
    public String eventName;
    public String eventEntity;
    // code if error
    public int statusCode;

    //QuerySeletcor, call other MSAs
    public Long queryId;
    public List<String> querySet;

    // time
    public LocalDateTime createdAt;

    public T transferProps;




}