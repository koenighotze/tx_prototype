package org.koenighotze.txprototype.user.controller.user;

import static java.util.UUID.randomUUID;
import static org.koenighotze.txprototype.user.controller.IanaRel.COLLECTION;
import static org.koenighotze.txprototype.user.resources.ResourceUtils.addLinksToModel;
import static org.koenighotze.txprototype.user.resources.UserResource.compareByLastAndFirstName;
import static org.springframework.hateoas.Link.REL_SELF;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

import javax.inject.*;
import javax.validation.*;
import javax.validation.constraints.*;

import io.vavr.*;
import io.vavr.collection.*;
import org.koenighotze.txprototype.user.messages.*;
import org.koenighotze.txprototype.user.model.*;
import org.koenighotze.txprototype.user.resources.*;
import org.springframework.hateoas.mvc.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.mvc.support.*;

@Controller
@RequestMapping(value = "/users", produces = TEXT_HTML_VALUE)
public class UserHtmlController {
    private static final String REDIRECT_USERS = "redirect:/users";
    private final UserCommandService commandService;
    private final UserQueryService queryService;
    private final FlashMessageUtil flashMessageUtil;

    @Inject
    public UserHtmlController(UserCommandService commandService, UserQueryService queryService, FlashMessageUtil flashMessageUtil) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.flashMessageUtil = flashMessageUtil;
    }

    @GetMapping
    public String showAllUsers(Model model) {
        List<UserResource> allUserResources = queryService.findAllUsers()
                                                          .map(UserResource::new);

        model.addAttribute("users", allUserResources.sorted(compareByLastAndFirstName())
                                                    .map(res -> Tuple.of(res.getUser(), res.getLink(REL_SELF)
                                                                                           .getHref())));

        UsersResource usersResource = new UsersResource(allUserResources);
        usersResource.add(linkToNewUserForm().withRel("create_form"));
        addLinksToModel(usersResource, model);

        return "users";
    }

    private ControllerLinkBuilder linkToNewUserForm() {
        return linkTo(methodOn(UserHtmlController.class).newUser(randomUUID().toString(), null));
    }

    @GetMapping("/{publicId}")
    public String showUser(@PathVariable String publicId, Model model, RedirectAttributes redirectAttributes) {
        return queryService.findByPublicId(publicId)
                           .map(user -> {
                               model.addAttribute("user", user);
                               addLinksToModel(new UserResource(user), model);
                               return "user";
                           })
                           .getOrElse(() -> {
                               flashMessageUtil.addFlashMessage(redirectAttributes, "error.user-not-found", publicId);
                               return REDIRECT_USERS;
                           });
    }

    @DeleteMapping(path = "/{publicId}", produces = TEXT_HTML_VALUE, consumes = APPLICATION_FORM_URLENCODED_VALUE)
    public String deleteUser(@PathVariable String publicId, RedirectAttributes redirectAttributes) {
        if (commandService.deleteUser(publicId)) {
            flashMessageUtil.addFlashMessage(redirectAttributes, "info.user-removed", publicId);
            return REDIRECT_USERS;
        }

        flashMessageUtil.addFlashMessage(redirectAttributes, "error.user-not-found", publicId);
        return REDIRECT_USERS;
    }

    @GetMapping("/new/{publicId}")
    public ModelAndView newUser(@PathVariable String publicId, Model model) {
        User user = new User(publicId, "", "", "", "");

        model.addAttribute("user", user);
        model.addAttribute("collection", new UserResource(user).getLink(COLLECTION.getRel())
                                                               .getHref());

        // Return ModelAndView otherwise cannot use HATEOAS link builder
        return new ModelAndView("new_user", model.asMap());
    }

    @PostMapping("/new/{publicId}")
    public String createUser(@Valid @NotNull @PathVariable String publicId, @Valid @NotNull User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "new_user";
        }

        return commandService.newUser(publicId, user)
                             .map(newUser -> {
                                 flashMessageUtil.addFlashMessage(redirectAttributes, "info.user-added",
                                                                  newUser.getUsername(), publicId);
                                 return REDIRECT_USERS;
                             })
                             .getOrElse(() -> {
                                 flashMessageUtil.addFlashMessage(redirectAttributes, "info.user-could-not-be-added");
                                 return REDIRECT_USERS;
                             });
    }

}
