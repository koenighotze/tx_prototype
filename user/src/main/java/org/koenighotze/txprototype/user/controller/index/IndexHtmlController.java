package org.koenighotze.txprototype.user.controller.index;

import static org.springframework.util.MimeTypeUtils.TEXT_HTML_VALUE;

import java.security.*;

import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/", produces = TEXT_HTML_VALUE)
public class IndexHtmlController {

    @RequestMapping
    public String home(Model model, Principal user) {
        if (user != null) {
            model.addAttribute("userName", user.getName());
        }

        return "index";
    }
}
