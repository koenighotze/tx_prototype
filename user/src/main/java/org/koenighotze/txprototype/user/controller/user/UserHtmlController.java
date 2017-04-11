package org.koenighotze.txprototype.user.controller.user;

import static javaslang.API.$;
import static javaslang.API.Case;
import static javaslang.API.Match;
import static org.koenighotze.txprototype.user.controller.IanaRel.COLLECTION;
import static org.koenighotze.txprototype.user.resources.ResourceUtils.addLinksToModel;
import static org.koenighotze.txprototype.user.resources.UserResource.compareByLastAndFirstName;
import static org.springframework.hateoas.Link.REL_SELF;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.PERMANENT_REDIRECT;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.function.*;
import javax.inject.*;
import javax.validation.*;
import javax.validation.constraints.*;

import javaslang.*;
import javaslang.collection.*;
import org.koenighotze.txprototype.user.model.*;
import org.koenighotze.txprototype.user.resources.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.*;

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
                Case($(), "redirect:/users"));
    }

    @RequestMapping(value = "/{publicId}", method = DELETE, produces = TEXT_HTML_VALUE)
    public String deleteUser(@PathVariable String publicId, RedirectAttributes redirectAttributes) {
        ResponseEntity<?> response = userRestController.deleteUserByPublicId(publicId);

        Function<HttpStatus, String> notFoundFunc = status -> {
            redirectAttributes.addFlashAttribute("message", "error.user-not-found");
            return "redirect:/users";
        };

        return Match(response.getStatusCode()).of(
                Case($(PERMANENT_REDIRECT), status -> {
                    redirectAttributes.addFlashAttribute("message", "info.user-removed");
                    return "redirect:/users";
                }),
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
    public String createUser(@Valid @NotNull @PathVariable String publicId, @Valid @NotNull User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "new_user";
        }
        userRestController.newUser(publicId, user);
        // TODO: replace placeholders
        redirectAttributes.addFlashAttribute("message", "info.user-added");
        return "redirect:/users";
    }

}