package org.koenighotze.txprototype.user.repository;

import static org.fest.assertions.Assertions.assertThat;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.txprototype.user.UserAdministrationApplication;
import org.koenighotze.txprototype.user.model.User;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author David Schmitz
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = UserAdministrationApplication.class)
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

        User found = userRepository.findOne(saved.getUserId());

        assertThat(found).isNotNull();

    }
}