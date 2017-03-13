package org.koenighotze.txprototype.user.controller;

import static java.util.Objects.requireNonNull;
import static org.koenighotze.txprototype.user.controller.IanaRel.COLLECTION;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.koenighotze.txprototype.user.model.User;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

/**
 * @author David Schmitz
 */
public class UserResource extends ResourceSupport {
    private User user;

    @JsonCreator
    public UserResource(@JsonProperty("user") User user) {
        this.user = requireNonNull(user);
        add(pointToApplication(linkTo(methodOn(UserRestController.class).getAllUsers()).withRel(COLLECTION.getRel())));
        add(pointToApplication(linkTo(methodOn(UserRestController.class, user.getPublicId()).userByPublicId(user.getPublicId())).withSelfRel()));
    }

    public Link pointToApplication(Link link) {
        return new Link(link.getHref().replace("\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}", "UserAdministration"), link.getRel());
    }

    public User getUser() {
        return user;
    }
}
