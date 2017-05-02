package org.koenighotze.txprototype.user.events;

import org.koenighotze.txprototype.user.model.*;

public class UserCreated extends UserEvent {
    private User user;

    public static UserCreated of(User user) {
        return new UserCreated(user);
    }

    private UserCreated(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserCreated{" + super.toString() + ", user=" + user + '}';
    }
}
