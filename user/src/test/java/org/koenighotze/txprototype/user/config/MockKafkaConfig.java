package org.koenighotze.txprototype.user.config;

import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.databind.*;
import org.koenighotze.txprototype.user.events.*;
import org.springframework.context.annotation.*;
import org.springframework.kafka.core.*;

@Configuration
@Profile("mock")
public class MockKafkaConfig {
    @Bean
    @Primary
    @SuppressWarnings("unchecked")
    public KafkaTemplate<String, UserCreatedEvent> kafkaTemplate(ObjectMapper objectMapper) {
        return (KafkaTemplate<String, UserCreatedEvent>) mock(KafkaTemplate.class);
    }
}
