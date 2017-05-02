package org.koenighotze.txprototype.user.controller.user;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.*;
import java.util.concurrent.*;

import javaslang.collection.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.*;
import org.junit.*;
import org.junit.runner.*;
import org.koenighotze.txprototype.user.*;
import org.koenighotze.txprototype.user.events.*;
import org.koenighotze.txprototype.user.model.*;
import org.koenighotze.txprototype.user.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.converter.*;
import org.springframework.http.converter.json.*;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.*;
import org.springframework.mock.http.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.context.web.*;
import org.springframework.test.web.servlet.*;
import org.springframework.util.concurrent.*;
import org.springframework.web.context.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = UserAdministrationApplication.class)
@WebAppConfiguration
@ActiveProfiles("mock")
public class UserRestControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaTemplate<String, UserEvent> kafkaTemplate;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        mappingJackson2HttpMessageConverter = List.of(converters)
                                                  .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                                                  .take(1)
                                                  .getOrElseThrow(() -> new RuntimeException("No Converter found!"));
    }

    //    @ClassRule
    //    public static KafkaEmbedded embeddedKafka =
    //        new KafkaEmbedded(1, true, "users");
    //
    private MockMvc mockMvc;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Before
    public void initKafkaTemplate() throws Exception {
        //            String kafkaBootstrapServers = embeddedKafka.getBrokersAsString();
        //            System.setProperty("kafka.bootstrap-servers", kafkaBootstrapServers);
        ListenableFuture<SendResult<String, UserEvent>> f = mock(ListenableFuture.class);
        RecordMetadata metaData = new RecordMetadata(new TopicPartition("users", 1), 12, 12, 12, 12, 1, 1);
        ProducerRecord<String, UserEvent> producerRecord = mock(ProducerRecord.class);
        when(f.get(anyInt(), any(TimeUnit.class))).thenReturn(new SendResult<>(producerRecord, metaData));
        when(kafkaTemplate.sendDefault(any(UserEvent.class))).thenReturn(f);

    }

    @Before
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void users_can_be_fetched() throws Exception {
        //@formatter:off
        mockMvc.perform(get("/users"))
               .andExpect(status().isOk());
        //@formatter:on
    }

    @Test
    public void the_users_list_is_prefilled() throws Exception {
        //@formatter:off
        mockMvc.perform(get("/users"))
               .andDo(log())
               .andExpect(content().contentType(APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$.users.length()", is(greaterThanOrEqualTo(3))));
        //@formatter:on
    }

    @Test
    public void get_a_single_user() throws Exception {
        User user = userRepository.save(new User("23", "first", "last", "flast", "foo@bar.de"));

        //@formatter:off
        mockMvc.perform(get("/users/" + user.getPublicId()))
               .andDo(log())
               .andExpect(content().contentType(APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$.user.firstname", is("first")))
               .andExpect(jsonPath("$.user.publicId", is("23")))
               .andExpect(jsonPath("$._links.collection.href", endsWith("/users")))
               .andExpect(jsonPath("$._links.self.href", endsWith("/users/" + user.getPublicId())));
        //@formatter:on
    }

    @Test
    @SuppressWarnings("unchecked")
    public void deleting_a_user_returns_ok() throws Exception {

        User user = userRepository.save(new User("23", "first", "last", "flast", "foo@bar.de"));
        //@formatter:off
        mockMvc.perform(delete("/users/{id}", user.getPublicId()))
               .andExpect(status().is(NO_CONTENT.value()));

        mockMvc.perform(get("/users/{id}", user.getPublicId()))
               .andExpect(status().is(NOT_FOUND.value()));
        //@formatter:on
    }

    @Test
    public void create_a_user_using_put() throws Exception {
        User user = new User(randomUUID().toString(), "First", "Last", "nick", "foo@bar.de");

        //@formatter:off
        mockMvc.perform(post("/users/{id}", user.getPublicId()).contentType(APPLICATION_JSON)
                                                              .content(json(user)))
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
        mockMvc.perform(put("/users/{id}", user.getPublicId()).contentType(APPLICATION_JSON)
                                                              .content(json(user)))
               .andExpect(status().isOk());
        //@formatter:on

        User found = userRepository.findByPublicId(user.getPublicId());
        assertThat(found).isNotNull();
        assertThat(found.getLastname()).isEqualTo("updated last name");
    }

    @SuppressWarnings("unchecked")
    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        mappingJackson2HttpMessageConverter.write(o, APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}