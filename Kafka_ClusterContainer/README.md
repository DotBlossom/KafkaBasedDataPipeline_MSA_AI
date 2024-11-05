### kafka cluster 내부


![Uploading sds.png…]()



### 비동기 로직 for DeferredResults or Partial RestCOntroller

         CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            // 비동기 함수 1 실행
            System.out.println("async 1...");
            sleep(1000); // 1초 대기
            return "결과 1";
        });

        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            // 비동기 함수 2 실행
            System.out.println("async 2 ...");
            sleep(2000); // 2초 대기
            return 2;
        });

        CompletableFuture<Boolean> future3 = CompletableFuture.supplyAsync(() -> {
            // 비동기 함수 3 실행
            System.out.println("async 3 ...");
            sleep(1500); // 1.5초 대기
            return true;
        });

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(future1, future2, future3);

        CompletableFuture<String> responseEntityFuture = allFutures.thenApply(v -> {
            try {
                String result1 = future1.get();
                Integer result2 = future2.get();
                Boolean result3 = future3.get();

                System.out.println("res 1 결과: " + result1);
                System.out.println("res 2 결과: " + result2);
                System.out.println("res 3 결과: " + result3);

                return result1; // result1을 ResponseEntity에 담아 반환
            } catch (InterruptedException | ExecutionException e) {

                return "codeError";
            }
        });


        String prov = responseEntityFuture.join();
        return ResponseEntity.ok(prov);
