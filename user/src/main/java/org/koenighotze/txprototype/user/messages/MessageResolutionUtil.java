package org.koenighotze.txprototype.user.messages;

import static java.util.Locale.getDefault;

import org.springframework.context.*;
import org.springframework.stereotype.*;

@Component
public class MessageResolutionUtil {
    private final MessageSource messageSource;

    public MessageResolutionUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String messageFor(String code, Object... args) {
        String message = messageSource.getMessage(code, args, "???" + code + "???", getDefault());

        return message;
    }
}
