package org.koenighotze.txprototype.user.events;

import static java.lang.Thread.currentThread;
import static java.util.concurrent.TimeUnit.*;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.concurrent.*;

import org.slf4j.*;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.*;
import org.springframework.stereotype.*;

@Component
public class UserEventProducer {
    private static final Logger logger = getLogger(UserEventProducer.class);

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    public UserEventProducer(KafkaTemplate<String, UserEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(UserEvent userEvent) {
        try {
            SendResult<String, UserEvent> result = kafkaTemplate.sendDefault(userEvent)
                                                                .get(1, SECONDS);
            logger.info("Send event type={}, id={}, meta={}", userEvent.getType(), userEvent.getId(), result.getRecordMetadata());
        } catch (InterruptedException | ExecutionException | TimeoutException e) { // TOOD handle the future semantic correctly
            logger.error("Cannot send event " + userEvent, e);
            currentThread().interrupt();
        }
    }
}
