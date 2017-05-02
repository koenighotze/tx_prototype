package org.koenighotze.txprototype.user.events;

import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

import java.time.*;

public abstract class UserEvent {
    private final String id;
    private final Instant createdOn;

    protected UserEvent(String id, Instant createdOn) {
        this.id = id;
        this.createdOn = createdOn;
    }

    protected UserEvent() {
        this(randomUUID().toString(), now());
    }

    public final String getId() {
        return id;
    }

    public final Instant getCreatedOn() {
        return createdOn;
    }

    @Override
    public String toString() {
        return "UserEvent{" + "createdOn=" + createdOn + ", id='" + id + '\'' + '}';
    }

    public final String getType() {
        return getClass().getSimpleName();
    }
}
