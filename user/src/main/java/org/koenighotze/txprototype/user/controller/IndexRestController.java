package org.koenighotze.txprototype.user.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import org.koenighotze.txprototype.user.resources.IndexResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author David Schmitz
 */
@Controller
@RequestMapping(value = "/", produces = APPLICATION_JSON_UTF8_VALUE)
public class IndexRestController {
    @RequestMapping
    public HttpEntity<IndexResource> index() {
        return new ResponseEntity<>(new IndexResource(), OK);
    }
}
