package com.microservice.userDetails.security;

import com.microservice.userDetails.configuration.RsaKeyProperties;
import com.microservice.userDetails.dao.User;
import com.microservice.userDetails.dto.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.Getter;
import lombok.Setter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Instant;
import java.util.Date;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Key;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Duration;
import java.util.Base64;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
public class JwtService {
    private final RSAPrivateKey rsaPrivateKey;
    private final RSAPublicKey rsaPublicKey;
    private final String keyId;


    public JwtService(RsaKeyProperties rsaKeyProperties) throws Exception {
        this.rsaPrivateKey = (RSAPrivateKey) loadKey(rsaKeyProperties.privateKeyLocation().getInputStream(), false);
        this.rsaPublicKey = (RSAPublicKey) loadKey(rsaKeyProperties.publicKeyLocation().getInputStream(), true);
        this.keyId = rsaKeyProperties.keyId();
    }

    public String generateToken(User user, Duration duration, TokenType tokenType)  {
        // Implementation for generating JWT token using rsaPrivateKey and keyId
        // This method should create a JWT token with the given subject, sign it with the private key,
        // and return the token as a String.
        // generate Access Token and Refresh Token
        return Jwts.builder()
                .setSubject(user.getUserName())
                .setExpiration(Date.from(Instant.now().plus(duration)))
                .setIssuedAt(Date.from(Instant.now()))
                .setId(keyId)
                .claim("typ",tokenType.name())
                .signWith(rsaPrivateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public Jws<Claims> validateToken(String token) {
        // Implementation for validating JWT token using rsaPublicKey
        // This method should parse the JWT token, verify its signature with the public key,
        // and return the claims if the token is valid.
        // If the token is invalid, it should throw an appropriate exception.
        return Jwts.parserBuilder()
                .setSigningKey(rsaPublicKey)
                .build()
                .parseClaimsJws(token);
    }

    private Key loadKey(InputStream stream, boolean isPublic) throws Exception {
        String key = new BufferedReader(new InputStreamReader(stream))
                .lines().collect(Collectors.joining()).replaceAll("-----\\w+ KEY-----", "").replaceAll("\\s", "");

        byte[] keyBytes = Base64.getDecoder().decode(key);
        KeyFactory factory = KeyFactory.getInstance("RSA");

        if (isPublic) {
            return factory.generatePublic(new X509EncodedKeySpec(keyBytes));
        } else {
            return factory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
        }
    }
}
