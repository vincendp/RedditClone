package com.vincendp.RedditClone.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PasswordEncoderConfiguration {

    @Bean
    public PasswordEncoder createPasswordEncoder(){
        PasswordEncoder defaults = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String encodingId = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(encodingId, new BCryptPasswordEncoder());

        DelegatingPasswordEncoder result = new DelegatingPasswordEncoder(encodingId, encoders);
        result.setDefaultPasswordEncoderForMatches(defaults);

        return result;
    }
}
