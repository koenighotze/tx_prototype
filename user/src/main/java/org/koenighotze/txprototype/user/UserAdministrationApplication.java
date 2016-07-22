package org.koenighotze.txprototype.user;

import org.koenighotze.txprototype.user.model.User;
import org.koenighotze.txprototype.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class UserAdministrationApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserAdministrationApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


//    @Bean
//    public LayoutDialect layoutDialect() {
//        return new LayoutDialect();
//    }

//    @Bean
//    public SpringTemplateEngine templateEngine() {
//        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//        templateEngine.setAdditionalDialects(singleton(new LayoutDialect()));
//        return templateEngine;
//    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository) {
        return evt -> {
            userRepository.deleteAll();
            userRepository.save(new User("david", "David", "Schmitz", "dschmitz", "dschmitz@foo.bar"));
            userRepository.save(new User("hugo", "Hugo", "Balder", "hbalder", "bhas@ds.dk"));
            userRepository.save(new User("samson", "Samson", "Oxen", "sox", "gesox@de.de"));
        };
    }
}
