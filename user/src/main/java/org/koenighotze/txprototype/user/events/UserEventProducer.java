package org.koenighotze.txprototype.user.events;

import static org.slf4j.LoggerFactory.getLogger;

import java.net.*;

import org.slf4j.*;
import org.springframework.stereotype.*;
import org.springframework.web.client.*;

@Component
public class UserEventProducer {
    private static final Logger logger = getLogger(UserEventProducer.class);

    public void publish(UserEvent userEvent) {
        RestTemplate f = new RestTemplate();
        URI uri = f.postForLocation("http://127.0.0.1:2113/streams/users", userEvent);
        logger.info("Posted event at {}", uri);
    }
}
