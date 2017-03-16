package org.koenighotze.txprototype.user.resources;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.koenighotze.txprototype.user.controller.IndexRestController;
import org.koenighotze.txprototype.user.controller.UserRestController;
import org.springframework.hateoas.ResourceSupport;

/**
 * @author David Schmitz
 */
public class IndexResource extends ResourceSupport {

    public IndexResource() {
        add(linkTo(UserRestController.class).withRel("users"));
        add(linkTo(methodOn(IndexRestController.class).index()).withSelfRel());
    }
}
