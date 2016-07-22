package org.koenighotze.txprototype.user.controller;

import javaslang.Tuple;
import javaslang.collection.Stream;
import javaslang.control.Option;
import org.koenighotze.txprototype.user.model.User;
import org.koenighotze.txprototype.user.repository.UserRepository;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;
import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static java.util.Comparator.comparing;
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
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

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
                        .sorted(compareByLastAndFirstName())
                        .map(res -> Tuple.of(res.getUser(), res.getLink(REL_SELF).getHref())));
        //@formatter:on
        return "users";
    }

    private Comparator<UserResource> compareByLastAndFirstName() {
        //@formatter:off
        return comparing((UserResource user) -> user.getUser().getLastname())
                .thenComparing((UserResource user) -> user.getUser().getFirstname());
        //@formatter:on
    }

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

        UserResource userResource = new UserResource(userToStore);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(userResource.getLink(REL_SELF).getHref()));

        HttpStatus status = Option.of(userToStore.getUserId()).map(id -> OK).getOrElse(CREATED);
        userRepository.save(userToStore);

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
                Case(Some($(instanceOf(User.class))), user -> new ResponseEntity<UserResource>(new UserResource(user), OK)),
                Case($(), new ResponseEntity<>(NOT_FOUND))
        );
        //@formatter:on
    }

    private Model addLinksToModel(ResourceSupport resource, Model model) {
        resource.getLinks().forEach(link -> model.addAttribute(link.getRel(), link.getHref()));
        return model;
    }

    @RequestMapping(value = "/{publicId}", method = GET, produces = TEXT_HTML_VALUE)
    public String showUser(@PathVariable String publicId, Model model) {
        ResponseEntity<UserResource> userResourceHttpEntity = userByPublicId(publicId);

        Function<? super HttpStatus, String> func = status -> {
            UserResource user = userResourceHttpEntity.getBody();
            model.addAttribute("user", user.getUser());
            addLinksToModel(user, model);
            return "user";
        };

        return Match(userResourceHttpEntity.getStatusCode()).of(
                Case($(OK), func),
                Case($(), "redirect:/users"));
    }

    @RequestMapping(value = "/{publicId}", method = DELETE, produces = TEXT_HTML_VALUE)
    public String deleteUser(@PathVariable String publicId, Model model, RedirectAttributes redirectAttributes) {
        ResponseEntity<?> response = deleteUserByPublicId(publicId);

        Function<HttpStatus, String> notFoundFunc = status -> {
            redirectAttributes.addFlashAttribute("message", "error.user-not-found");
            return "redirect:/users";
        };

        return Match(response.getStatusCode()).of(
                Case($(PERMANENT_REDIRECT), "redirect:/users"),
                Case($(NOT_FOUND), notFoundFunc)
        );
    }

    @RequestMapping(value = "/new", method = GET, produces = TEXT_HTML_VALUE)
    public String newUser(Model model) {
        User user = new User();
        user.setPublicId(UUID.randomUUID().toString());
        model.addAttribute("user", user);
        model.addAttribute("collection", new UserResource(user).getLink(COLLECTION.getRel()).getHref());

        return "new_user";
    }

    @RequestMapping(value = "/new", method = POST, produces = TEXT_HTML_VALUE)
    public String createUser(User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "new_user";
        }

        System.err.println("Should store " + user);
        Thread.dumpStack();

//        userRepository.save(user);
        redirectAttributes.addFlashAttribute("message", "error.user-added");
        return "redirect:/users";
    }

}
