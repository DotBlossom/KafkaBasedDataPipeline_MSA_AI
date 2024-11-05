package com.example.kafkaAsyncTest.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resultId;

    private String eventType;

    private int transactionalEventLevel;
    private int currentEventLevel;

    @ElementCollection
    @CollectionTable(name = "id_container", joinColumns = @JoinColumn(name="result_id"))
    @Column(name = "event_related_id")
    private List<Long> eventRelatedIds = new ArrayList<>();

    @Override
    public String toString() {
        return "Result{" +
                "resultId=" + resultId +
                ", eventType='" + eventType + '\'' +
                ", transactionalEventLevel=" + transactionalEventLevel +
                ", currentEventLevel=" + currentEventLevel +
                ", eventRelatedIds=" + eventRelatedIds +
                '}';
    }


}
