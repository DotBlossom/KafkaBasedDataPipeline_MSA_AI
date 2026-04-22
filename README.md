### 정리용 다이어그램
추후 Backend, Devops, AI 구독자에 각각 필요한 Diagram 제공

### AI에 원하는 임베딩 전구체 주는 데이터 파이프라인

![12121 drawio](https://github.com/user-attachments/assets/11be9ba9-3863-4429-bd40-86927db1720e)



# MSA 위한 비동기 통신 서버 Framework
> **MSA Component & Cluster Container**

본 프로젝트는 MSA(Microservice Architecture) 레이어에 **AOP 및 공용 모듈 코드를 이식**하여, 서비스 간 **비동기 통신**을 효율적으로 설계하고 구현하는 것을 목표로 합니다. 이를 통해 코드 통합 과정의 간소화와 시스템 확장성 확보를 지향합니다.

---

##  개요 (Overview)
MSA 환경에서 서비스 간 통신 시 발생할 수 있는 복잡한 요청 체인을 관리하고, 비동기 처리를 통해 서버 자원 효율을 극대화합니다. 특히 전송의 중요도에 따라 전략을 분리하여 시스템의 유연성을 높였습니다.

## 주요 구성 요소 (Architecture)

### 1. MSAComponent
* 각 개별 MSA 서버에 적용되는 통신 전용 컴포넌트입니다.
* 비동기 요청 발생 및 결과 반환을 담당합니다.

### 2. ClusterContainer
* Kafka를 통해 전달되는 요청 체인을 관리합니다.
* 관련 이벤트 및 트랜잭션을 정의하며, 비동기 요청을 취합하여 관리하는 중앙 컨트롤러 역할을 수행합니다.

> **Note:** 두 요소는 Kafka Pub/Sub 설정, AOP 로직, Security Context 관리 코드를 공유하여 일관된 통신 규격을 유지합니다.

---

## 🔄 작동 과정 (Workflow)

기본적으로 MSA Component에서 발생한 이벤트에 대해 결과가 반환되는 구조이며, 상세 과정은 다음과 같습니다.

1. **이벤트 감지 및 가로채기**:
   * MSA Component에서 이벤트가 발생하면 `ExportToKafkaProducerEventCollectionAspect`가 실행됩니다.
   * 정의된 이벤트 규칙(Rule)에 따라 AOP를 통해 해당 이벤트를 감지합니다.

2. **메시지 발행**:
   * 가로챈 이벤트는 설정된 규칙에 따라 Kafka Cluster 또는 대상 MSA 서버로 전달됩니다.

3. **이벤트 체인 및 관리 전략**:
   * **복잡한 체인 이벤트**: 관리가 필요한 복합 이벤트의 경우, `ClusterContainer` 내의 단일 컨트롤러에서 비동기 요청을 취합하여 구현 및 관리합니다.
   * **단순 이벤트**: 대상 MSA Component로 즉시 전송하며, 결과값이나 단순 트랜잭션 이력만 `ClusterContainer`에서 관리합니다.

---

## ✅ 진행도 및 성과 (Progress & Achievements)

* **통신 구조 설계**: 전체 MSA 레이어 간의 통신 구조 설계 및 이식 가능한 공통 모듈화를 완료하였습니다.
* **비동기 객체 호출 테스트**: `CompletableFuture` 및 `DeferredResult` 기반의 비동기 요청/응답 구조를 구현하고 동작을 검증하였습니다.
* **보안 통합 성공**: 쓰레드 상속 및 전환 과정에서 발생하는 **Security 토큰 인증 정보 휘발 문제 없이** 정상적으로 인증 정보가 유지됨을 확인하였습니다.
* **확장성 확보**: 비동기 서블릿 기술을 적용하여 클라이언트에게 영향을 주지 않으면서도 서버의 확장성(Scalability)을 극대화하는 최적의 방식을 채택하였습니다.

> **향후 계획**: 상세한 분산 트랜잭션 구조 및 이벤트 블록(Event Block) 구현은 메인 서버의 완성도에 맞추어 통합 진행할 예정입니다.
##  주요 구현 상세 (Implementation Details)

### 1. 전송 보장 전략 분리 (Exactly-once vs Default)
데이터의 성격에 따라 정확한 통신 또는 속도 지향 통신을 선택적으로 적용할 수 있습니다.
* **Exactly-once**: 메시지 중복 없는 정확한 전송 보장 (결제, 주문 등 중요 트랜잭션).
* **Default**: 높은 처리 속도 지향 (단순 로그, 알림 등).
* `ContainerFactory`에서 사전 정의된 프로퍼티를 적용하고 토픽 이름을 분리하여 관리합니다.

### 2. 비동기 요청 처리 구조
`DeferredResult` 및 `CompletableFuture` 기반으로 설계하여 서블릿 쓰레드 점유 없이 응답을 대기합니다.

```java
@PostMapping("/async")
public CompletableFuture<ResponseEntity<String>> exportPropagation(@RequestBody Result res) {

    CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
        // 비동기 작업 실행
        return "결과 1";
    }, CACHED_THREAD_POOL);

    CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
        return 2;
    }, CACHED_THREAD_POOL);

    CompletableFuture<Boolean> future3 = CompletableFuture.supplyAsync(() -> {
        return true;
    }, CACHED_THREAD_POOL);

    // 비동기 작업 취합 및 응답 (논블로킹)
    return CompletableFuture.allOf(future1, future2, future3)
        .thenApply(v -> {
            // thenApply 내에서 결과를 조합하여 반환 (쓰레드 점유 최소화)
            String result1 = future1.join(); 
            
            // 클라이언트(React 등)는 기존 ResponseEntity 포맷 그대로 수신 가능
            return ResponseEntity.ok(result1); 
        });
}
```
### 3. Security Context 유지 (인증 정보 상속)
비동기 쓰레드 전환 시 `ThreadLocal` 기반의 Security 토큰 인증 정보가 휘발되는 문제를 해결하기 위해, `RequestAttributeSecurityContextRepository`를 적용하여 `HttpServletRequest` 생명주기 동안 인증 정보를 안정적으로 유지합니다.

```java
.securityContext((securityContext) -> securityContext
    .securityContextRepository(new DelegatingSecurityContextRepository(
        new RequestAttributeSecurityContextRepository() // HttpServletRequest 속성에 인증 정보 저장
    ))
)
```




==================================================================================

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

![ssss (1)](https://github.com/user-attachments/assets/b4dd1728-bd71-4e2a-8948-cfdfda3d9ee5)



### temp

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


![ssdfafaffa drawio](https://github.com/user-attachments/assets/3e0dd6ad-727a-4bf2-9821-5167c0f579d5)


