package org.koenighotze.txprototype.user.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.koenighotze.txprototype.user.model.User;
import org.springframework.hateoas.ResourceSupport;

import static java.util.Objects.requireNonNull;
import static org.koenighotze.txprototype.user.controller.IanaRel.COLLECTION;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * @author David Schmitz
 */
public class UserResource extends ResourceSupport {
    private User user;

    @JsonCreator
    public UserResource(@JsonProperty("user") User user) {
        this.user = requireNonNull(user);
        add(linkTo(methodOn(UserRestController.class).getAllUsers()).withRel(COLLECTION.getRel()));
        add(linkTo(methodOn(UserRestController.class, user.getUserId()).userByPublicId(user.getPublicId())).withSelfRel());
    }

    public User getUser() {
        return user;
    }
}
