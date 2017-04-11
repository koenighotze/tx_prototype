package org.koenighotze.txprototype.user.events;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

import java.time.*;

import com.fasterxml.jackson.annotation.*;
import org.koenighotze.txprototype.user.model.*;

public class UserCreatedEvent {
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventCreatedTime;
    private User user;

    public UserCreatedEvent() {
    }

    public static UserCreatedEvent createdNow(User user) {
        return new UserCreatedEvent(LocalDateTime.now(), user);
    }

    private UserCreatedEvent(LocalDateTime eventCreatedTime, User user) {
        this.eventCreatedTime = eventCreatedTime;
        this.user = user;
    }

    public LocalDateTime getEventCreatedTime() {
        return eventCreatedTime;
    }

    public void setEventCreatedTime(LocalDateTime eventCreatedTime) {
        this.eventCreatedTime = eventCreatedTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserCreatedEvent{" + "eventCreatedTime=" + eventCreatedTime + ", user=" + user + '}';
    }
}
