package org.koenighotze.txprototype.user.messages;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.*;
import org.springframework.context.*;

public class MessageResolutionUtilTest {

    private MessageSource messageSource;

    @Before
    public void setupMessageSource() {
        messageSource = mock(MessageSource.class);
        when(
            messageSource.getMessage(eq("known_code"), any(Object[].class), anyString(), any(Locale.class))).thenReturn(
            "the known message");
        when(messageSource.getMessage(eq("unkown_code"), any(Object[].class), eq("???unkown_code???"),
            any(Locale.class))).thenReturn("???unkown_code???");
    }

    @Test
    public void the_resolved_message_is_returned() {
        MessageResolutionUtil util = new MessageResolutionUtil(messageSource);

        String result = util.messageFor("known_code");

        assertThat(result).isEqualTo("the known message");
    }

    @Test
    public void the_default_message_is_returned_if_the_key_is_unknown() {
        MessageResolutionUtil util = new MessageResolutionUtil(messageSource);

        String result = util.messageFor("unkown_code");

        assertThat(result).isEqualTo("???unkown_code???");
    }
}