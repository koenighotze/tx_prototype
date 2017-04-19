package org.koenighotze.txprototype.user.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.*;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;

@Configuration
@EnableOAuth2Sso
public class Oauth2Config extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //@formatter:off
        http.authorizeRequests()
            .antMatchers("/*", "/login**", "/bootstrap/**", "/font-awesome/**", "/fonts/**", "/js/**").permitAll()
            .antMatchers("/users**").hasRole("USERS")
            .antMatchers("/admin**").hasRole("ADMIN")
            .anyRequest().authenticated()
            .and()
              .logout().logoutSuccessUrl("/").permitAll();
        //@formatter:on
    }
}
