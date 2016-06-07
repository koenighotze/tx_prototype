package org.koenighotze.txprototype.user;

import org.koenighotze.txprototype.user.model.User;
import org.koenighotze.txprototype.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UserAdministrationApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserAdministrationApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository) {
        return evt -> {
            userRepository.deleteAll();
            userRepository.save(new User("David", "Schmitz", "dschmitz", "dschmitz@foo.bar"));
            userRepository.save(new User("Hugo", "Balder", "hbalder", "bhas@ds.dk"));
            userRepository.save(new User("Samson", "Oxen", "sox", "gesox@de.de"));
        };
    }
}
