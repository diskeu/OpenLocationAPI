package dev.timjelenz.openlocationapi.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration specialised on security utilites.
 */
@Configuration
public class SecurityConfig {
    /**
     * Primary password encoder.
     * 
     * @return Bcrypt Password encoder
     */
    @Primary
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
