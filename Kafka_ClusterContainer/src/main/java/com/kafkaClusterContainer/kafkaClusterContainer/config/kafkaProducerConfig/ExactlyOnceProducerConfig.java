package com.kafkaClusterContainer.kafkaClusterContainer.config.kafkaProducerConfig;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ExactlyOnceProducerConfig {
    // ExactlyOnce 전략 : no transaction, At least Once + duplicationFilterChecker


    // properties-injections

    @Value("${kafkaPipeline.bootstrap-servers}")
    private String bootstrap="";

    @Value("${kafkaPipeline.topic-name-1}")
    private String topic_1_name="";



    // topic-indications

    @Bean
    public NewTopic createTopic(){
        return new NewTopic(topic_1_name, 3, (short) 1);
    }


    // set - configuration
    // configHashMap -> Factory -> Template

    public Map<String, Object> exactlyOnceProducerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        // 고민.. dup prob minimizing?
        // 도입 가능한 이유 : key를 사용해서 > 동일 이벤트는 동일한 partition으로 가게해서 순서보장하기에.
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        // 고민.. 그래도 분리된 factory 특성이니까, 보장해주자
        props.put(ProducerConfig.ACKS_CONFIG, "all");

        return props;
    }

    @Bean
    public ProducerFactory<String,Object> exactlyOnceproducerFactory(){
        return new DefaultKafkaProducerFactory<>(exactlyOnceProducerConfig());
    }

    @Bean
    public KafkaTemplate<String,Object> exactlyOnceKafkaTemplate(){
        return new KafkaTemplate<>(exactlyOnceproducerFactory());
    }
}


