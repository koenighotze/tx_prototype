package org.koenighotze.txprototype.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.*;

import org.junit.*;
import org.junit.runner.*;
import org.koenighotze.txprototype.user.*;
import org.koenighotze.txprototype.user.model.*;
import org.springframework.boot.autoconfigure.mongo.embedded.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.junit4.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {UserAdministrationApplication.class, EmbeddedMongoAutoConfiguration.class})
public class UserRepositoryTest {
    @Inject
    private UserRepository userRepository;

    @Test
    public void find_all_users_returns_users() {
        assertThat(userRepository.findAll()).isNotNull();
    }

    @Test
    public void store_and_retrieve_user() {
        User saved = userRepository.save(new User("23", "David", "Schmitz", "dschmitz", "schmitz@foo.de"));
        assertThat(saved.getUserId()).isNotNull();

        User found = userRepository.findById(saved.getUserId())
                                   .orElse(null);

        assertThat(found).isNotNull();
    }
}