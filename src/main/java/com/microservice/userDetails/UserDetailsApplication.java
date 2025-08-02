package com.microservice.userDetails;

import com.microservice.userDetails.configuration.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
// The @EnableJpaAuditing annotation is used to enable JPA auditing features in the application.
// JPA auditing allows you to automatically populate fields like created date, last modified date, etc.
// This is useful for tracking changes to entities in the database without having to manually set these fields.
@EnableJpaAuditing
public class UserDetailsApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserDetailsApplication.class, args);
    }

}
