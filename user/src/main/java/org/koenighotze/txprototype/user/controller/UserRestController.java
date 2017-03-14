package org.koenighotze.txprototype.user.controller;

import static javaslang.API.$;
import static javaslang.API.Case;
import static javaslang.API.Match;
import static javaslang.Patterns.Some;
import static javaslang.Predicates.instanceOf;
import static org.koenighotze.txprototype.user.controller.IanaRel.COLLECTION;
import static org.springframework.hateoas.Link.REL_SELF;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.PERMANENT_REDIRECT;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.net.URI;
import javax.inject.Inject;

import javaslang.collection.List;
import javaslang.control.Option;
import org.koenighotze.txprototype.user.model.User;
import org.koenighotze.txprototype.user.repository.UserRepository;
import org.koenighotze.txprototype.user.resources.UserResource;
import org.koenighotze.txprototype.user.resources.UsersResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author David Schmitz
 */
@Controller
@RequestMapping(value = "/users", produces = APPLICATION_JSON_UTF8_VALUE)
public class UserRestController {

    private final UserRepository userRepository;

    @Inject
    public UserRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(method = GET)
    public HttpEntity<UsersResource> getAllUsers() {
        //@formatter:off
        return new ResponseEntity<>(
                new UsersResource(List.ofAll(userRepository.findAll())
                        .map(UserResource::new)), OK);
        //@formatter:on
    }

    // example PUT for creating resource
    @RequestMapping(value = "/{publicId}", method = PUT, consumes = APPLICATION_JSON_UTF8_VALUE)
    public HttpEntity<UserResource> newUser(@PathVariable String publicId, @RequestBody User user) {
        //@formatter:off
        User userToStore = Option.of(userRepository.findByPublicId(publicId))
                .map(foundUser -> {
                    foundUser.setEmail(user.getEmail());
                    foundUser.setLastname(user.getLastname());
                    foundUser.setFirstname(user.getFirstname());
                    return foundUser;
                })
                .getOrElse(user);
        //@formatter:on

        HttpStatus status = Option.of(userToStore.getUserId()).map(id -> OK).getOrElse(CREATED);
        userRepository.save(userToStore);

        UserResource userResource = new UserResource(userToStore);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(userResource.getLink(REL_SELF).getHref()));

        return new ResponseEntity<>(userResource, httpHeaders, status);
    }

    @RequestMapping(value = "/{publicId}", method = DELETE)
    public ResponseEntity<?> deleteUserByPublicId(@PathVariable String publicId) {
        //@formatter:off
        Option<User> userOption = Option.of(userRepository.findByPublicId(publicId));

        Option<HttpHeaders> result =
                userOption
                        .map(user -> {
                            userRepository.delete(user);
                            return user;
                        })
                        .map(UserResource::new)
                        .map(userResource -> userResource.getLink(COLLECTION.getRel()).getHref())
                        .map(href -> {
                            HttpHeaders httpHeaders = new HttpHeaders();
                            httpHeaders.setLocation(URI.create(href));
                            return httpHeaders;
                        });

        // TODO: react to errors
        return Match(result).of(
                Case(Some($(instanceOf(HttpHeaders.class))), h -> new ResponseEntity<>(null, h, PERMANENT_REDIRECT)),
                Case($(), new ResponseEntity<>(null, NOT_FOUND))
        );
        //@formatter:on
    }

    @RequestMapping(value = "/{publicId}", method = GET)
    public ResponseEntity<UserResource> userByPublicId(@PathVariable String publicId) {
        //@formatter:off
        return Match(Option.of(userRepository.findByPublicId(publicId))).of(
                Case(Some($(instanceOf(User.class))), user -> new ResponseEntity<>(new UserResource(user), OK)),
                Case($(), new ResponseEntity<>(NOT_FOUND))
        );
        //@formatter:on
    }


}
