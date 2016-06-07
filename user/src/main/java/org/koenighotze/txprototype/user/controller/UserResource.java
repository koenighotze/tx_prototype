package org.koenighotze.txprototype.user.controller;

import static java.util.Objects.requireNonNull;
import static org.koenighotze.txprototype.user.controller.IanaRel.COLLECTION;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.koenighotze.txprototype.user.model.User;
import org.springframework.hateoas.ResourceSupport;

/**
 * @author David Schmitz
 */
public class UserResource extends ResourceSupport {
    private final User user;

    public UserResource(User user) {
        this.user = requireNonNull(user);
        add(linkTo(methodOn(UserRestController.class).getAllUsers()).withRel(COLLECTION.getRel()));
        add(linkTo(methodOn(UserRestController.class, user.getUserId()).userById(user.getUserId())).withSelfRel());
    }

    public User getUser() {
        return user;
    }
}
