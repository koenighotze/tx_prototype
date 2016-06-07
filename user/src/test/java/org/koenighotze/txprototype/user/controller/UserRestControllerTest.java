package org.koenighotze.txprototype.user.controller;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.txprototype.user.UserAdministrationApplication;
import org.koenighotze.txprototype.user.model.User;
import org.koenighotze.txprototype.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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
        User user = userRepository.save(new User("first", "last", "flast", "foo@bar.de"));

        //@formatter:off
        mockMvc
            .perform(get("/users/" + user.getUserId()))
            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.user.firstname", is("first")))
            .andExpect(jsonPath("$._links.collection.href", endsWith("/users")))
            .andExpect(jsonPath("$._links.self.href", endsWith("/users/" + user.getUserId())));
        //@formatter:on
    }

    @Test
    public void deleting_a_user_returns_ok() throws Exception {
        User user = userRepository.save(new User("first", "last", "flast", "foo@bar.de"));
        //@formatter:off
        mockMvc
            .perform(delete("/users/{id}", user.getUserId()))
            .andExpect(status().isOk());

        mockMvc
            .perform(get("/users/{id}", user.getUserId()))
            .andExpect(status().is(NOT_FOUND.value()));
        //@formatter:on
    }
}