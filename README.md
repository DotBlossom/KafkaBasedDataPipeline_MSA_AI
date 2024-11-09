### 정리용 다이어그램
추후 Backend, Devops, AI 구독자에 각각 필요한 Diagram 제공

### AI에 원하는 임베딩 전구체 주는 데이터 파이프라인

![12121 drawio](https://github.com/user-attachments/assets/11be9ba9-3863-4429-bd40-86927db1720e)


### json-selector-key:value

        {
          "clothes": {
            "category": ["01outer_01coat", "01outer_02jacket"],
            "material": ["synthetic fiber Others"],
            "color": ["khaki", "Black"],
            "neck_design": ["hood"],
            "pant나 다른 카테고리속성" : [],
            "top_length_type": ["long"],
            "sleeve_length_type": ["long sleeves"],
            "brand": ["US-ARMY"],
            "price": [2190000],
            "size": ["S", "M", "L"],
            "gender": ["unisex"],
            "season": ["winter"], 
            "pattern": ["None"], 
            "style": ["casual"],
            "fit": ["loose"], 
            "shing_instructions": ["dry cleaning"],
            "specific_context" : ["내피: 탈부착 가능 내피",
        			"패딩 충전재: 5온스 패딩 솜",
        			"어깨 디자인: 드롭 숄더"]
        		}
        }


### 전체 코드 로직 요약

![12121 drawio](https://github.com/user-attachments/assets/26b1a5f8-e8a5-4376-b247-982755753e2e)




![제목 없는 다이어그램 drawio (1)](https://github.com/user-attachments/assets/b1351fdc-c500-4b4d-b38d-e7661b7f5beb)

    ExternalCluster
    - MSA ConsumerMapper : InnerProp - event controller - Transaction Block integrate
    - Alarm Agg -> push Polling API
    - AI data integration -> AI Cluster || otherData Agg


#### MSA 1차 케이스 분류

![dddd drawio](https://github.com/user-attachments/assets/ef11b990-a548-42af-a516-d223dd64c680)

#### Async Listener - Callable 구조

![defeer drawio](https://github.com/user-attachments/assets/fd8385dc-7c11-475b-ae01-b6f2619a446f)




### 카프카 Prod, Consumer를 Default (at least once), Exatly Once(default + ack + Reply + Filter)로 구성
    MSA 내부 호출 (Async propagation 등)에서 한번만 서비스가 호출되는건 매우 중요한 일.


### 주의사항:
    Config 객체를 형성할때, bean annotation을 사용할 필요가없음.
    CustonConfig 여러개를 관리하게되면, 상이한 이름이라도, 동일한 config객체로 인식함.


### Test: 서로 다른 팩토리Context객체를 각자 독립적으로 실행가능.
![afaffaafafafaf](https://github.com/user-attachments/assets/9a7d970d-5010-4dbe-913b-74ef06de9bcf)

### jwt propagations - diff Thread (async)
    securityChain에 delegated 추가.
    또한, 다른 정보가 필요하다면, context를 Thread 상속으로 유지가능.

### DeferredResult + ListenableProps || CompleteFuture.Supply ...  
    온전한 비동기 호출 property 구현완료


### external cluster transactinal block router
    transacrionalLevelTraxker 값으로 url처럼 앞에서 라우팅

