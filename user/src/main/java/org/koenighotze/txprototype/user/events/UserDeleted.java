package org.koenighotze.txprototype.user.events;

import org.koenighotze.txprototype.user.model.*;

public class UserDeleted extends UserEvent {
    private final String publicId;

    public UserDeleted(String publicId) {
        this.publicId = publicId;
    }

    public String getPublicId() {
        return publicId;
    }

    @Override
    public String toString() {
        return "UserDeleted{" + "publicId='" + publicId + '\'' + "} " + super.toString();
    }

    public static UserDeleted of(User user) {
        return new UserDeleted(user.getPublicId());
    }

}
