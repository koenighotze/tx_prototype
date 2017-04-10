package org.koenighotze.txprototype.user.controller.index;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import org.koenighotze.txprototype.user.resources.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/", produces = APPLICATION_JSON_UTF8_VALUE)
public class IndexRestController {
    @RequestMapping
    public HttpEntity<IndexResource> index() {
        return new ResponseEntity<>(new IndexResource(), OK);
    }
}
