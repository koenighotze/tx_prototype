package org.koenighotze.txprototype.user.controller;

import static java.util.Comparator.comparing;
import static javaslang.API.$;
import static javaslang.API.Case;
import static javaslang.API.Match;
import static javaslang.Patterns.Some;
import static javaslang.Predicates.instanceOf;
import static org.springframework.hateoas.Link.REL_SELF;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.net.URI;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import org.koenighotze.txprototype.user.model.User;
import org.koenighotze.txprototype.user.repository.UserRepository;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javaslang.Tuple;
import javaslang.collection.Stream;
import javaslang.control.Option;

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
    public HttpEntity<List<UserResource>> getAllUsers() {
        //@formatter:off
        return new ResponseEntity<>(
            Stream
              .ofAll(userRepository.findAll())
              .map(UserResource::new)
              .toJavaList(), OK);
        //@formatter:on
    }

    @RequestMapping(method = GET, produces = TEXT_HTML_VALUE)
    public String showAllUsers(Model model) {
        //@formatter:off
        model.addAttribute(
            "users",
            Stream.ofAll(getAllUsers().getBody())
                  .sorted(userResourceComparator())
                  .map(res -> Tuple.of(res.getUser(), res.getLink(REL_SELF).getHref())));
        //@formatter:on
        return "users";
    }

    private Comparator<UserResource> userResourceComparator() {
        //@formatter:off
        return comparing((UserResource user) -> user.getUser().getLastname())
               .thenComparing((UserResource user) -> user.getUser().getFirstname());
        //@formatter:on
    }

    @RequestMapping(value = "/{publicId}", method = DELETE)
    public ResponseEntity<?> deleteUserByPublicId(@PathVariable String publicId) {
        //@formatter:off
        Option<User> userOption = Option.of(userRepository.findByPublicId(publicId));

        Option<HttpHeaders> result =
            userOption
            .map(user -> { userRepository.delete(user); return user; })
            .map(UserResource::new)
            .map(userResource -> userResource.getLink(Link.REL_SELF).getHref())
            .map(href -> {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setLocation(URI.create(href));
                return httpHeaders;
            });

        return Match(result).of(
          Case(Some($(instanceOf(HttpHeaders.class))), h -> new ResponseEntity<>(null, h, OK)),
          Case($(), new ResponseEntity<>(null, NOT_FOUND))
        );
        //@formatter:on
    }

    @RequestMapping(value = "/{publicId}", method = GET)
    public HttpEntity<UserResource> userByPublicId(@PathVariable String publicId) {
        //@formatter:off
        return Match(Option.of(userRepository.findByPublicId(publicId))).of(
          Case(Some($(instanceOf(User.class))), user -> new ResponseEntity<UserResource>(new UserResource(user), OK)),
          Case($(), new ResponseEntity<>(NOT_FOUND))
        );
        //@formatter:on
    }

    @RequestMapping(value = "/{publicId}", method = GET, produces = TEXT_HTML_VALUE)
    public String showUser(@PathVariable String publicId, Model model) {
        UserResource user = userByPublicId(publicId).getBody();
        model.addAttribute("user", user.getUser());
        model.addAttribute("collection", user.getLink("collection").getHref());
        return "user";
    }
}
