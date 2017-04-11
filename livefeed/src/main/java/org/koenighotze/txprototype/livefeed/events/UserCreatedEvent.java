package org.koenighotze.txprototype.livefeed.events;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

import java.time.*;

import com.fasterxml.jackson.annotation.*;
import org.koenighotze.txprototype.livefeed.model.*;

public class UserCreatedEvent {
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventCreatedTime;
    private UserReadModel userReadModel;

    private UserCreatedEvent(@JsonProperty("eventCreatedTime") LocalDateTime eventCreatedTime, @JsonProperty("user") UserReadModel userReadModel) {
        this.eventCreatedTime = eventCreatedTime;
        this.userReadModel = userReadModel;
    }

    public LocalDateTime getEventCreatedTime() {
        return eventCreatedTime;
    }

    public UserReadModel getUser() {
        return userReadModel;
    }

    @Override
    public String toString() {
        return "UserCreatedEvent{" + "eventCreatedTime=" + eventCreatedTime + ", user=" + userReadModel + '}';
    }
}
