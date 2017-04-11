package org.koenighotze.txprototype.livefeed.resources;

import static java.util.Objects.requireNonNull;

import org.koenighotze.txprototype.livefeed.model.*;
import org.springframework.hateoas.*;

public class UserReadModelResource extends ResourceSupport {
    private UserReadModel user;

    public UserReadModelResource(UserReadModel user) {
        this.user = requireNonNull(user);
    }

    public UserReadModel getUser() {
        return user;
    }
}
