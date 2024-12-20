package com.kafkaClusterContainer.kafkaClusterContainer.entity;

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
    // transactionIndicator : 여기에 저장후, 어디 event를 MSA에 줄지.. 등등을 담은,
    // 일단은 result에 돌아오는거니까... 거기에 구현..
    // MSA -> Data -> MSa
    private String transactionIndicator;

    //
    private Long resultId;
    private String EventType;

    @ElementCollection
    @CollectionTable(name = "id_container", joinColumns = @JoinColumn(name="event_block_id"))
    @Column(name = "event_related_id")
    private List<Long> eventRelatedIds = new ArrayList<>();



    private boolean eventEndFlag = false;

    @Override
    public String toString() {
        return "eventBlock : {"
                + "eventBlockId:" + eventBlockId +
                "totalEventBlockChecker:" + totalEventBlockChecker +
                "depth:" + mainDepth + " / " + currentDepth +
                "EventTransaction: {" + "prev :" + prevEventTransaction +
                "curr :" + currentEventTransaction + " } " +
                "transactionIndicator: " + transactionIndicator +
                "eventEndFlag: " + eventEndFlag +
                "}";
    }

}

