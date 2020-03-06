package com.vincendp.RedditClone.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private PasswordEncoder passwordEncoder;
    private UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfiguration(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService){
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable().authorizeRequests().antMatchers("/*").permitAll();
//        http.antMatcher("/users").authorizeRequests().anyRequest().authenticated();
        http.authorizeRequests()
        .anyRequest().authenticated().and().formLogin();
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
//    }


}
