package org.koenighotze.txprototype.user.resources;

import static java.util.UUID.randomUUID;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import io.vavr.collection.*;
import org.koenighotze.txprototype.user.controller.user.*;
import org.springframework.hateoas.*;

public class UsersResource extends ResourceSupport {
    private final List<UserResource> users;

    public UsersResource(List<UserResource> users) {
        this.users = users;
        add(linkTo(methodOn(UserRestController.class).getAllUsers()).withSelfRel());
        add(linkTo(methodOn(UserRestController.class).newUser(randomUUID().toString(), null)).withRel("create_new"));
    }

    public List<UserResource> getUsers() {
        return users;
    }
}
