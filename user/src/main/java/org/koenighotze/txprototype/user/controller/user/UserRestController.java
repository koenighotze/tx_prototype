package org.koenighotze.txprototype.user.controller.user;

import static javaslang.API.$;
import static javaslang.API.Case;
import static javaslang.API.Match;
import static javaslang.Patterns.Some;
import static javaslang.Predicates.instanceOf;
import static org.springframework.hateoas.Link.REL_SELF;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.net.*;
import javax.inject.*;

import org.koenighotze.txprototype.user.model.*;
import org.koenighotze.txprototype.user.resources.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users", produces = APPLICATION_JSON_UTF8_VALUE)
//@PreAuthorize("hasRole('ROLE_USER')")
public class UserRestController {

    private final UserCommandService commandService;
    private final UserQueryService queryService;

    @Inject
    public UserRestController(UserCommandService commandService, UserQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @RequestMapping(method = GET)
    public HttpEntity<UsersResource> getAllUsers() {
        return new ResponseEntity<>(new UsersResource(queryService.findAllUsers()
                                                                  .map(UserResource::new)), OK);
    }

    @RequestMapping(value = "/{publicId}", method = POST, consumes = APPLICATION_JSON_VALUE)
    public HttpEntity<UserResource> newUser(@PathVariable String publicId, @RequestBody User user) {
        return commandService.newUser(publicId, user)
                             .map(createdUser -> {
                                 UserResource userResource = new UserResource(createdUser);
                                 HttpHeaders httpHeaders = new HttpHeaders();
                                 httpHeaders.setLocation(URI.create(userResource.getLink(REL_SELF)
                                                                                .getHref()));
                                 return new ResponseEntity<>(userResource, httpHeaders, CREATED);
                             })
                             .getOrElse(new ResponseEntity<>(BAD_REQUEST));
    }

    @RequestMapping(value = "/{publicId}", method = PUT, consumes = APPLICATION_JSON_VALUE)
    public HttpEntity<UserResource> updateUser(@PathVariable String publicId, @RequestBody User user) {
        return commandService.updateUser(publicId, user)
                             .map(updatedUser -> {
                                 UserResource userResource = new UserResource(updatedUser);
                                 return new ResponseEntity<>(userResource, OK);
                             })
                             .getOrElse(new ResponseEntity<>(BAD_REQUEST));
    }

    @RequestMapping(value = "/{publicId}", method = DELETE)
    public HttpEntity<Void> deleteUserByPublicId(@PathVariable String publicId) {
        if (!commandService.deleteUser(publicId)) {
            return new ResponseEntity<>(NOT_FOUND);
        }

        return new ResponseEntity<>(NO_CONTENT);
    }

    @RequestMapping(value = "/{publicId}", method = GET)
    public ResponseEntity<UserResource> userByPublicId(@PathVariable String publicId) {
        //@formatter:off
        return Match(queryService.findByPublicId(publicId)).of(
                Case(Some($(instanceOf(User.class))), user -> new ResponseEntity<>(new UserResource(user), OK)),
                Case($(), new ResponseEntity<>(NOT_FOUND))
        );
        //@formatter:on
    }
}
