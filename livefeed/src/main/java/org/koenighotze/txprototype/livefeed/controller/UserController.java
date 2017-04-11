package org.koenighotze.txprototype.livefeed.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import javax.inject.*;

import javaslang.collection.*;
import org.koenighotze.txprototype.livefeed.repository.*;
import org.koenighotze.txprototype.livefeed.resources.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users", produces = APPLICATION_JSON_UTF8_VALUE)
public class UserController {
    private final UserReadModelRepository userReadModelRepository;

    @Inject
    public UserController(UserReadModelRepository userReadModelRepository) {
        this.userReadModelRepository = userReadModelRepository;
    }

    @RequestMapping(method = GET)
    public HttpEntity<UserReadModelsResource> getAllUsers() {
        return new ResponseEntity<>(new UserReadModelsResource(List.ofAll(userReadModelRepository.findAll())
                                                                  .map(UserReadModelResource::new)), OK);
    }
}
