package com.example.kafkaAsyncTest.config.kafkaProducerConfig;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

public class DefaultProducerConfig {

    @Value("${kafkaPipeline.bootstrap-servers}")
    private String bootstrap = "";

    @Value("${kafkaPipeline.topic-name-2}")
    private String topic_2_name = "";



    // topic-indications

    @Bean
    public NewTopic createTopic(){
        return new NewTopic(topic_2_name, 3, (short) 1);
    }


    // set - configuration
    // configHashMap -> Factory -> Template


    public Map<String, Object> defaultProducerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return props;
    }

    @Bean
    public ProducerFactory<String,Object> defaultProducerFactory(){
        return new DefaultKafkaProducerFactory<>(defaultProducerConfig());
    }

    @Bean
    public KafkaTemplate<String,Object> defaultKafkaTemplate(){
        return new KafkaTemplate<>( defaultProducerFactory());
    }
}
