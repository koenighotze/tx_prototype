package org.koenighotze.txprototype.user.controller;

import javaslang.Tuple;
import javaslang.collection.Stream;
import org.koenighotze.txprototype.user.model.User;
import org.koenighotze.txprototype.user.resources.UserResource;
import org.koenighotze.txprototype.user.resources.UsersResource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.function.Function;

import static java.util.Comparator.comparing;
import static javaslang.API.$;
import static javaslang.API.Case;
import static javaslang.API.Match;
import static org.koenighotze.txprototype.user.controller.IanaRel.COLLECTION;
import static org.springframework.hateoas.Link.REL_SELF;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.PERMANENT_REDIRECT;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author David Schmitz
 */
@Controller
@RequestMapping(value = "/users", produces = TEXT_HTML_VALUE)
public class UserHtmlController {

    private final UserRestController userRestController;

    @Inject
    public UserHtmlController(UserRestController userRestController) {
        this.userRestController = userRestController;
    }

    @RequestMapping(method = GET)
    public String showAllUsers(Model model) {
        UsersResource allUsers = userRestController.getAllUsers().getBody();
        //@formatter:off
        model.addAttribute(
                "users",
                Stream.ofAll(allUsers.getUsers())
                        .sorted(compareByLastAndFirstName())
                        .map(res -> Tuple.of(res.getUser(), res.getLink(REL_SELF).getHref())));
        addLinksToModel(allUsers, model);
        //@formatter:on
        return "users";
    }

    private Comparator<UserResource> compareByLastAndFirstName() {
        //@formatter:off
        return comparing((UserResource user) -> user.getUser().getLastname())
                .thenComparing((UserResource user) -> user.getUser().getFirstname());
        //@formatter:on
    }

    private Model addLinksToModel(ResourceSupport resource, Model model) {
        resource.getLinks().forEach(link -> model.addAttribute(link.getRel(), link.getHref()));
        return model;
    }

    @RequestMapping(value = "/{publicId}", method = GET)
    public String showUser(@PathVariable String publicId, Model model) {
        ResponseEntity<UserResource> userResourceHttpEntity = userRestController.userByPublicId(publicId);

        Function<? super HttpStatus, String> func = status -> {
            UserResource user = userResourceHttpEntity.getBody();
            model.addAttribute("user", user.getUser());
            addLinksToModel(user, model);
            return "user";
        };

        return Match(userResourceHttpEntity.getStatusCode()).of(
                Case($(OK), func),
                Case($(), "redirect:/getUsers"));
    }

    @RequestMapping(value = "/{publicId}", method = DELETE, produces = TEXT_HTML_VALUE)
    public String deleteUser(@PathVariable String publicId, RedirectAttributes redirectAttributes) {
        ResponseEntity<?> response = userRestController.deleteUserByPublicId(publicId);

        Function<HttpStatus, String> notFoundFunc = status -> {
            redirectAttributes.addFlashAttribute("message", "error.user-not-found");
            return "redirect:/getUsers";
        };

        return Match(response.getStatusCode()).of(
                Case($(PERMANENT_REDIRECT), "redirect:/getUsers"),
                Case($(NOT_FOUND), notFoundFunc)
        );
    }

    @RequestMapping(value = "/new/{publicId}", method = GET)
    public String newUser(@PathVariable String publicId, Model model) {
        User user = new User(publicId, "", "", "", "");
        model.addAttribute("user", user);
        model.addAttribute("collection", new UserResource(user).getLink(COLLECTION.getRel()).getHref());
        return "new_user";
    }

    @RequestMapping(value = "/new/{publicId}", method = POST)
    public String createUser(@PathVariable String publicId, User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "new_user";
        }
        userRestController.newUser(publicId, user);
        // TODO: replace placeholders
        redirectAttributes.addFlashAttribute("message", "info.user-added");
        return "redirect:/users";
    }

}
