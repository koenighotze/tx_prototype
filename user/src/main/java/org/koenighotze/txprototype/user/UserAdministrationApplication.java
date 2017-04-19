package org.koenighotze.txprototype.user;

import java.security.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.*;
import javaslang.jackson.datatype.*;
import org.koenighotze.txprototype.user.model.*;
import org.koenighotze.txprototype.user.repository.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.*;
import org.thymeleaf.extras.springsecurity4.dialect.*;

@SpringBootApplication
@RestController
public class UserAdministrationApplication { // extends WebSecurityConfigurerAdapter {
    public static void main(String[] args) {
        SpringApplication.run(UserAdministrationApplication.class, args);
    }

    @RequestMapping("/session")
    public Principal user(Principal principal) {
        return principal;
    }

    @Bean
    public SpringSecurityDialect securityDialect() {
        return new SpringSecurityDialect();
    }

    @Bean
    //    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository) {
        return evt -> {
            userRepository.deleteAll();
            userRepository.save(new User("david", "David", "Schmitz", "dschmitz", "dschmitz@foo.bar"));
            userRepository.save(new User("hugo", "Hugo", "Balder", "hbalder", "bhas@ds.dk"));
            userRepository.save(new User("samson", "Samson", "Oxen", "sox", "gesox@de.de"));
        };
    }



    @Bean
    public static Module javaslangModule() {
        return new JavaslangModule();
    }

    @Bean
    public static Module jsr310Module() {
        return new JavaTimeModule();
    }
}
