package org.koenighotze.txprototype.user.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.*;

import org.junit.*;
import org.springframework.http.*;
import org.springframework.http.converter.json.*;
import org.springframework.mock.http.client.*;

/**
 * @author David Schmitz
 */
public class UserDeserialisationTest {

    @Test
    public void deserialize_json() throws IOException {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        User user = (User) converter.read(User.class, new MockClientHttpResponse("{\"publicId\":\"david\",\"firstname\":\"David\",\"lastname\":\"Schmitz\",\"username\":\"dschmitz\",\"email\":\"dschmitz@foo.bar\"}".getBytes(), HttpStatus.OK));

        assertThat(user.getFirstname()).isEqualTo("David");
    }

}