package org.koenighotze.txprototype.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

/**
 * @author David Schmitz
 */
@Controller
@RequestMapping(value = "/layout", produces = TEXT_HTML_VALUE)
public class LayoutTestController {
    @RequestMapping(method = RequestMethod.GET)
    public String showAllUsers(Model model) {
        return "layouttest";
    }
}
