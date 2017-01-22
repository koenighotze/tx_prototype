package org.koenighotze.txprototype.user.controller;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author David Schmitz
 */
@Controller
@RequestMapping(value = "/", produces = TEXT_HTML_VALUE)
public class IndexHtmlController {
    @RequestMapping
    public String home() {
        return "index";
    }
}
