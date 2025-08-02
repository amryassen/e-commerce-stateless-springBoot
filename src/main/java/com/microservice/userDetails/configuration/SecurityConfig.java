package com.microservice.userDetails.configuration;

import com.microservice.userDetails.security.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // This method configures the security filter chain for the application.
        // It can be customized to define security rules, such as which endpoints are secured,
        // what authentication mechanisms are used, etc.
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                        .requestMatchers("/auth/**").permitAll())
                // "/.well-known/jwks.json" we can put this if we separated the Authorization Server and Resource Server
                // and we want to expose the public key for the Resource Server to validate the JWT tokens
                // .requestMatchers("/.well-known/jwks.json").permitAll()


                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> {}))
                .build();
    }

    @Bean
    public JwtDecoder myDecoder(JwtService jwtService) {
        // This method returns a custom JWT decoder that can be used to decode and validate JWT tokens.
        // The implementation of this method should return an instance of JwtDecoder that uses the RSA public key
        // for verifying the JWT signatures.
        return NimbusJwtDecoder.withPublicKey(jwtService.getRsaPublicKey())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // This method returns a PasswordEncoder that can be used to encode and verify passwords.
        // The implementation of this method should return an instance of PasswordEncoder, such as BCryptPasswordEncoder.
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }
}
