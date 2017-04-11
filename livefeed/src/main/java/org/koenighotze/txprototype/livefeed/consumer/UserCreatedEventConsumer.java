package org.koenighotze.txprototype.livefeed.consumer;

import static org.slf4j.LoggerFactory.getLogger;

import javax.inject.*;

import org.koenighotze.txprototype.livefeed.events.*;
import org.koenighotze.txprototype.livefeed.model.*;
import org.koenighotze.txprototype.livefeed.repository.*;
import org.slf4j.*;
import org.springframework.kafka.annotation.*;
import org.springframework.stereotype.*;

@Component
public class UserCreatedEventConsumer {
    private static final Logger logger = getLogger(UserCreatedEventConsumer.class);

    private final UserReadModelRepository userReadModelRepository;

    @Inject
    public UserCreatedEventConsumer(UserReadModelRepository userReadModelRepository) {
        this.userReadModelRepository = userReadModelRepository;
    }

    @KafkaListener(topics = "users")
    public void userCreated(UserCreatedEvent event) {
        logger.info("Received {}", event);

        UserReadModel user = event.getUser();
        user.setCreatedTimestamp(event.getEventCreatedTime());
        userReadModelRepository.save(user);
    }
}
