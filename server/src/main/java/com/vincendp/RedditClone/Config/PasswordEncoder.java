package com.vincendp.RedditClone.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PasswordEncoder {

    @Bean
    public org.springframework.security.crypto.password.PasswordEncoder createPasswordEncoder(){
        org.springframework.security.crypto.password.PasswordEncoder defaults = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String encodingId = "bcrypt";
        Map<String, org.springframework.security.crypto.password.PasswordEncoder> encoders = new HashMap<>();
        encoders.put(encodingId, new BCryptPasswordEncoder());

        DelegatingPasswordEncoder result = new DelegatingPasswordEncoder(encodingId, encoders);
        result.setDefaultPasswordEncoderForMatches(defaults);

        return result;
    }
}
