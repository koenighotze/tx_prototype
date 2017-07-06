package org.koenighotze.txprototype.user;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.*;
import io.vavr.jackson.datatype.*;
import org.koenighotze.txprototype.user.model.*;
import org.koenighotze.txprototype.user.repository.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.thymeleaf.*;
import org.springframework.context.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.*;
import org.thymeleaf.extras.springsecurity4.dialect.*;

@RestController
// deactivate 2.x autoconfig
@SpringBootApplication(exclude = {ThymeleafAutoConfiguration.class})
@ComponentScan("org.koenighotze.txprototype.user")
public class UserAdministrationApplication { // extends WebSecurityConfigurerAdapter {
    public static void main(String[] args) {
        SpringApplication.run(UserAdministrationApplication.class, args);
    }

//    @RequestMapping("/session")
//    public Principal user(Principal principal) {
//        return principal;
//    }

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
    public Module vavrModule() {
        return new VavrModule();
    }

    @Bean
    public Module jsr310Module() {
        return new JavaTimeModule();
    }
}
