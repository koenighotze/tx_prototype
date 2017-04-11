package org.koenighotze.txprototype.livefeed.resources;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javaslang.collection.*;
import org.koenighotze.txprototype.livefeed.controller.*;
import org.springframework.hateoas.*;

public class UserReadModelsResource extends ResourceSupport {
    private final List<UserReadModelResource> users;

    public UserReadModelsResource(List<UserReadModelResource> users) {
        this.users = users;
        add(linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
    }

    public List<UserReadModelResource> getUsers() {
        return users;
    }
}
