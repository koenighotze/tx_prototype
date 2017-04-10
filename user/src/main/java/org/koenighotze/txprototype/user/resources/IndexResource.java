package org.koenighotze.txprototype.user.resources;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.koenighotze.txprototype.user.controller.index.IndexRestController;
import org.koenighotze.txprototype.user.controller.user.UserRestController;
import org.springframework.hateoas.ResourceSupport;

public class IndexResource extends ResourceSupport {

    public IndexResource() {
        add(linkTo(UserRestController.class).withRel("users"));
        add(linkTo(methodOn(IndexRestController.class).index()).withSelfRel());
    }
}
