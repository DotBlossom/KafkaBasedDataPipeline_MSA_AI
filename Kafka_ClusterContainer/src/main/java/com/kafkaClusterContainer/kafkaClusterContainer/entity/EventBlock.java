package com.kafkaClusterContainer.kafkaClusterContainer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventBlockId;

    // check only BinaryType , just has been completed?
    private String totalEventBlockChecker;


    // eventTracker Depth
    private int mainDepth;
    private int currentDepth;

    // prev-curr supporting Indications

    private String prevEventTransaction;
    private String currentEventTransaction;


    // 16 hexType or binary snapshot.

    private String transactionIndicator;


    @Override
    public String toString() {
        return "eventBlock : {"
                + "eventBlockId:" + eventBlockId +
                "totalEventBlockChecker:" + totalEventBlockChecker +
                "depth:" + mainDepth + " / " + currentDepth +
                "EventTransaction: {" + "prev :" + prevEventTransaction +
                "curr :" + currentEventTransaction + " } " +
                "transactionIndicator: " + transactionIndicator +
                "}";
    }

}

