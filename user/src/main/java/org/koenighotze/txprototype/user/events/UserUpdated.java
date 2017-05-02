package org.koenighotze.txprototype.user.events;

import org.koenighotze.txprototype.user.model.*;

public class UserUpdated extends UserEvent {
    private final String publicId;
    private final User updateData;

    private UserUpdated(String publicId, User updateData) {
        this.publicId = publicId;
        this.updateData = updateData;
    }

    public static UserUpdated of(String publicId, User updateData) {
        return new UserUpdated(publicId, updateData);
    }

    public String getPublicId() {
        return publicId;
    }

    public User getUpdateData() {
        return updateData;
    }

    @Override
    public String toString() {
        return "UserUpdated{" + "publicId='" + publicId + '\'' + ", updateData=" + updateData + "} " + super.toString();
    }
}
