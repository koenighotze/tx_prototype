package org.koenighotze.txprototype.user.messages;

import org.springframework.stereotype.*;
import org.springframework.web.servlet.mvc.support.*;

@Component
public class FlashMessageUtil {
    private final MessageResolutionUtil messageResolutionUtil;

    public FlashMessageUtil(MessageResolutionUtil messageResolutionUtil) {
        this.messageResolutionUtil = messageResolutionUtil;
    }

    public RedirectAttributes addFlashMessage(RedirectAttributes redirectAttributes, String messageCode, Object... args) {
        redirectAttributes.addFlashAttribute("message", messageResolutionUtil.messageFor(messageCode, args));

        return redirectAttributes;
    }
}
