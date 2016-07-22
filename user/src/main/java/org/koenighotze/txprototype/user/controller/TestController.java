package org.koenighotze.txprototype.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author David Schmitz
 */
@Controller
@RequestMapping(value = "/test", produces = TEXT_HTML_VALUE)
public class TestController {

    @RequestMapping(method = GET)
    public String foo() {
        return "test";
    }
}
