package org.koenighotze.txprototype.user.resources;

import static java.util.Comparator.comparing;
import static org.koenighotze.txprototype.user.controller.IanaRel.COLLECTION;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import org.koenighotze.txprototype.user.controller.user.*;
import org.koenighotze.txprototype.user.model.*;
import org.springframework.hateoas.*;

public class UserResource extends ResourceSupport {
    private User user;

    @JsonCreator
    public UserResource(@JsonProperty("user") User user) {
        this();

        this.user = Objects.requireNonNull(user);
        add(linkTo(methodOn(UserRestController.class).userByPublicId(user.getPublicId())).withSelfRel());
    }

    public UserResource() {
        add(linkTo(methodOn(UserRestController.class).getAllUsers()).withRel(COLLECTION.getRel()));
    }

    public static Comparator<UserResource> compareByLastAndFirstName() {
        //@formatter:off
        return comparing((UserResource user) -> user.getUser().getLastname())
                .thenComparing((UserResource user) -> user.getUser().getFirstname());
        //@formatter:on
    }

    public User getUser() {
        return user;
    }
}
