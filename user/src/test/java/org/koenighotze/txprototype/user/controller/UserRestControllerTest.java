package org.koenighotze.txprototype.user.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.txprototype.user.UserAdministrationApplication;
import org.koenighotze.txprototype.user.model.User;
import org.koenighotze.txprototype.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;

import static java.util.UUID.randomUUID;
import static org.fest.assertions.Assertions.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author David Schmitz
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = UserAdministrationApplication.class)
@WebAppConfiguration
public class UserRestControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    private MockMvc mockMvc;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter =
            Arrays.asList(converters).stream().filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();
    }

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void users_can_be_fetched() throws Exception {
        //@formatter:off
        mockMvc
            .perform(get("/users"))
            .andExpect(status().isOk());
         //@formatter:on
    }

    @Test
    public void the_users_list_is_prefilled() throws Exception {
        //@formatter:off
        mockMvc
            .perform(get("/users"))
            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.length()", is(greaterThanOrEqualTo(3))));
        //@formatter:on
    }

    @Test
    public void get_a_single_user() throws Exception {
        User user = userRepository.save(new User("23", "first", "last", "flast", "foo@bar.de"));

        //@formatter:off
        mockMvc
            .perform(get("/users/" + user.getPublicId()))
            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.user.firstname", is("first")))
            .andExpect(jsonPath("$.user.publicId", is("23")))
            .andExpect(jsonPath("$._links.collection.href", endsWith("/users")))
            .andExpect(jsonPath("$._links.self.href", endsWith("/users/" + user.getPublicId())));
        //@formatter:on
    }

    @Test
    public void deleting_a_user_returns_ok() throws Exception {
        User user = userRepository.save(new User("23", "first", "last", "flast", "foo@bar.de"));
        //@formatter:off
        mockMvc
            .perform(delete("/users/{id}", user.getPublicId()))
            .andExpect(status().isOk());

        mockMvc
            .perform(get("/users/{id}", user.getPublicId()))
            .andExpect(status().is(NOT_FOUND.value()));
        //@formatter:on
    }

    @Test
    public void create_a_user_using_put() throws Exception {
        User user = new User(randomUUID().toString(), "First", "Last", "nick", "foo@bar.de");

        //@formatter:off
        mockMvc
            .perform(put("/users/{id}", user.getPublicId())
              .contentType(APPLICATION_JSON)
              .content(json(user))
            )
            .andExpect(status().is(CREATED.value()));
        //@formatter:on

        User found = userRepository.findByPublicId(user.getPublicId());
        assertThat(found).isNotNull();
    }

    @Test
    public void update_a_user_using_put() throws Exception {
        User user = new User(randomUUID().toString(), "First", "Last", "nick", "foo@bar.de");
        userRepository.save(user);

        user.setLastname("updated last name");

        //@formatter:off
        mockMvc
            .perform(put("/users/{id}", user.getPublicId())
              .contentType(APPLICATION_JSON)
              .content(json(user))
            )
            .andExpect(status().isOk());
        //@formatter:on

        User found = userRepository.findByPublicId(user.getPublicId());
        assertThat(found).isNotNull();
        assertThat(found.getLastname()).isEqualTo("updated last name");

    }

    @SuppressWarnings("unchecked")
    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        mappingJackson2HttpMessageConverter.write(o, APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}