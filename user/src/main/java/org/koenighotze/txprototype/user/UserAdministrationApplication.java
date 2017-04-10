package org.koenighotze.txprototype.user;

import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_DOC;

import java.util.*;

import com.fasterxml.jackson.databind.*;
import javaslang.jackson.datatype.*;
import org.apache.kafka.common.serialization.*;
import org.koenighotze.txprototype.user.model.*;
import org.koenighotze.txprototype.user.repository.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.*;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.web.client.*;

@SpringBootApplication
public class UserAdministrationApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserAdministrationApplication.class, args);
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        DefaultKafkaProducerFactory<String, String> factory = new DefaultKafkaProducerFactory<>(producerConfigs());

        factory.setKeySerializer(new StringSerializer());
        factory.setValueSerializer(new JsonSerializer<>());

        return factory;
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(VALUE_SERIALIZER_CLASS_DOC, "org.apache.kafka.common.serialization.StringDeserializer");
        return props;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    //    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository) {
        return evt -> {
            userRepository.deleteAll();
            userRepository.save(new User("david", "David", "Schmitz", "dschmitz", "dschmitz@foo.bar"));
            userRepository.save(new User("hugo", "Hugo", "Balder", "hbalder", "bhas@ds.dk"));
            userRepository.save(new User("samson", "Samson", "Oxen", "sox", "gesox@de.de"));
        };
    }

    @Bean
    public static Module javaslangModule() {
        return new JavaslangModule();
    }
}
