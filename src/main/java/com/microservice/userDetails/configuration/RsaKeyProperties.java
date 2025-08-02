package com.microservice.userDetails.configuration;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "jwt.rsa")
// This annotation binds the properties prefixed with "jwt.rsa" in application.properties or application.yml to
// this class. It allows you to define properties related to RSA key configuration.
// The properties can include things like key size, key id, public key location, private key location, etc.
// This class is used to encapsulate those properties for easy access and management within the application.
public record RsaKeyProperties(Resource publicKeyLocation, Resource privateKeyLocation, String keyId) {

    // The 'publicKey' and 'privateKey' fields are of type 'Resource', which is a Spring framework type
    // that represents a resource (like a file or classpath resource). These fields will hold the locations
    // of the RSA public and private keys respectively.
    // The 'keyId' field is a String that can be used to identify the key, often used in JWTs for signing and verification.
    // The use of 'record' in Java is a feature that allows for a concise way to create immutable data classes.
}
