package org.koenighotze.txprototype.user.model;



import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.client.MockClientHttpResponse;

public class UserDeserializationTest {

    @Test
    public void deserialize_json() throws IOException {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        User user = (User) converter.read(User.class, new MockClientHttpResponse("{\"publicId\":\"david\",\"firstname\":\"David\",\"lastname\":\"Schmitz\",\"username\":\"dschmitz\",\"email\":\"dschmitz@foo.bar\"}".getBytes(), HttpStatus.OK));

        assertThat(user.getFirstname()).isEqualTo("David");
    }

}