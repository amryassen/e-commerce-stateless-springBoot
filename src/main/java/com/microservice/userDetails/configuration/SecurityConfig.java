package com.microservice.userDetails.configuration;

import com.microservice.userDetails.security.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // This method configures the security filter chain for the application.
        // It can be customized to define security rules, such as which endpoints are secured,
        // what authentication mechanisms are used, etc.
        // we disable CSRF here because we are using JWT for stateless authentication
        // and CSRF protection is not needed in this case.
        // If you are using session-based authentication, you might want to enable CSRF protection.
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                        .requestMatchers("/auth/**").permitAll())

                // "/.well-known/jwks.json" we can put this if we separated the Authorization Server and Resource Server
                // and we want to expose the public key for the Resource Server to validate the JWT tokens
                // .requestMatchers("/.well-known/jwks.json").permitAll()


                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                        ))
                .build();
    }

    /**
     * Custom JWT Authentication Converter
     * Extracts "roles" claim and converts it to Spring Security authorities (ROLE_XXX format).
     *
     */
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // This converter extracts the "roles" claim from the JWT and converts it to Spring Security
        // Granted authorities. The roles will be prefixed with "ROLE_" to match Spring Security's
        // authority format.
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");

        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        // This method sets the JwtGrantedAuthoritiesConverter to the JwtAuthenticationConverter.
        jwtConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            // Get default roles
            Collection<GrantedAuthority> authorities =
                    jwtGrantedAuthoritiesConverter.convert(jwt).stream()
                            .map(grantedAuthority -> new SimpleGrantedAuthority(grantedAuthority.getAuthority()))
                            .collect(Collectors.toSet());

            // Optionally merge other custom claims into authorities
            // e.g., from a claim named "permissions"
            List<String> extraPermissions = jwt.getClaimAsStringList("permissions");
            if (extraPermissions != null) {
                authorities.addAll(extraPermissions.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList());
            }
            return authorities;
        });
        return jwtConverter;
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
