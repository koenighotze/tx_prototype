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
        http.csrf().disable().authorizeRequests().antMatchers("/**").permitAll();
//        http.authorizeRequests()
//            .antMatchers("/users/new/*").hasRole("CRM_ADMIN")
//            .antMatchers("/users**").hasRole("USER")
//            // TODO Add pattern that restricts on HTTP verb
//            .antMatchers("/*", "/login**", "/bootstrap/**", "/font-awesome/**", "/fonts/**", "/js/**").permitAll()
//            .anyRequest().authenticated()
//            .and()
//              .logout().logoutSuccessUrl("/").permitAll();
        //@formatter:on
    }
}
