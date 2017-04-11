package org.koenighotze.txprototype.livefeed;

import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.*;
import javaslang.jackson.datatype.*;
import org.apache.kafka.common.serialization.*;
import org.koenighotze.txprototype.livefeed.events.*;
import org.koenighotze.txprototype.livefeed.repository.*;
import org.slf4j.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.*;
import org.springframework.kafka.annotation.*;
import org.springframework.kafka.config.*;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@EnableKafka
@SpringBootApplication
public class LivefeedApplication {
    private static final Logger logger = getLogger(LivefeedApplication.class);

    @Bean
    public static Module javaslangModule() {
        return new JavaslangModule();
    }

    @Bean
    public static Module jsr310Module() {
        return new JavaTimeModule();
    }

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, UserCreatedEvent>> kafkaListenerContainerFactory(ObjectMapper objectMapper) {
        ConcurrentKafkaListenerContainerFactory<String, UserCreatedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(objectMapper));
        factory.setConcurrency(1);
        factory.getContainerProperties()
               .setPollTimeout(1000);
        // factory.setRetryTemplate(null);
        // factory.setRecoveryCallback(null);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, UserCreatedEvent> consumerFactory(ObjectMapper objectMapper) {
        DefaultKafkaConsumerFactory<String, UserCreatedEvent> factory = new DefaultKafkaConsumerFactory<>(
            consumerConfigs());

        factory.setValueDeserializer(new JsonDeserializer<>(UserCreatedEvent.class, objectMapper));
        factory.setKeyDeserializer(new StringDeserializer());

        return factory;
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(GROUP_ID_CONFIG, "livestream");
        props.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }

    @Bean
    CommandLineRunner commandLineRunner(UserReadModelRepository userReadModelRepository) {
        return evt -> {
            if (evt.length > 0 && "purge".equals(evt[0])) {
                logger.info("Purging local database...");
                userReadModelRepository.deleteAll();
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(LivefeedApplication.class, args);
    }
}
