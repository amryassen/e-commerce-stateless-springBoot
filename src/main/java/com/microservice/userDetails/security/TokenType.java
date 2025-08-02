package com.microservice.userDetails.security;

public enum TokenType {
    ACCESS_TOKEN,
    REFRESH_TOKEN;

    // This enum defines two types of tokens: ACCESS_TOKEN and REFRESH_TOKEN.
    // It can be used to differentiate between the two types when generating or validating JWTs.
}
