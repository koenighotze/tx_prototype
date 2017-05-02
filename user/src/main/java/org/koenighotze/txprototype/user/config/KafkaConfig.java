package org.koenighotze.txprototype.user.config;

import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.MAX_BLOCK_MS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG;

import java.util.*;

import com.fasterxml.jackson.databind.*;
import org.apache.kafka.common.serialization.*;
import org.koenighotze.txprototype.user.events.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@Profile("!mock")
public class KafkaConfig {
    @Value("${kafka.bootstrapServersConfig}")
    private String bootstrapServersConfig;

    @Value("${kafka.requestTimeoutMs}")
    private Integer requestTimeoutMs;

    @Value("${kafka.maxBlockMs}")
    private Integer maxBlockMs;

    @Bean
    public ProducerFactory<String, UserEvent> producerFactory(ObjectMapper objectMapper) {
        DefaultKafkaProducerFactory<String, UserEvent> factory = new DefaultKafkaProducerFactory<>(
            producerConfigs());

        factory.setKeySerializer(new StringSerializer());
        factory.setValueSerializer(new JsonSerializer<>(objectMapper));

        return factory;
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServersConfig);
        props.put(REQUEST_TIMEOUT_MS_CONFIG, requestTimeoutMs);
        props.put(MAX_BLOCK_MS_CONFIG, maxBlockMs);
        return props;
    }

    @Bean
    public KafkaTemplate<String, UserEvent> kafkaTemplate(ObjectMapper objectMapper) {
        KafkaTemplate<String, UserEvent> kafkaTemplate = new KafkaTemplate<>(
            producerFactory(objectMapper));
        kafkaTemplate.setDefaultTopic("user");
        return kafkaTemplate;
    }

}
