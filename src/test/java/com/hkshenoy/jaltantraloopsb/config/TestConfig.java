package com.hkshenoy.jaltantraloopsb.config;

import com.hkshenoy.jaltantraloopsb.security.CustomUserDetailsService;
import com.hkshenoy.jaltantraloopsb.security.UserRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {
    @Bean
    public CustomUserDetailsService customUserDetailsService(UserRepository userRepository) {
        return new CustomUserDetailsService(userRepository);
    }
}

