package com.microservice.userDetails.service;

import com.microservice.userDetails.dao.User;
import com.microservice.userDetails.dto.AuthResponse;
import com.microservice.userDetails.dto.UserLoginDto;
import com.microservice.userDetails.dto.UserRegistrationDto;
import com.microservice.userDetails.mapper.UserMapper;
import com.microservice.userDetails.repository.UserRepository;
import com.microservice.userDetails.security.JwtService;
import com.microservice.userDetails.security.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class JwtAuthService implements AuthService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse register(UserRegistrationDto userRegistrationDto) {
        // Convert UserRegistrationDto to User entity
        User user = userMapper.fromRegistrationDto(userRegistrationDto);
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        userRepository.save(user);

        // Generate JWT tokens
        String accessToken = jwtService.generateToken(user, Duration.ofMinutes(15), TokenType.ACCESS_TOKEN);
        String refreshToken = jwtService.generateToken(user, Duration.ofDays(7), TokenType.REFRESH_TOKEN);

        // Return the AuthResponse with the tokens
        return generateTokens(user);
    }

    @Override
    public AuthResponse login(UserLoginDto userLoginDto) {
        return userRepository.findByUsername(userLoginDto.getUserName())
                .filter(user -> passwordEncoder.matches(userLoginDto.getPassword(), userLoginDto.getPassword()))
                .map(this::generateTokens)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
    }

    private AuthResponse generateTokens(User user) {
        String accessToken = jwtService.generateToken(user, Duration.ofMinutes(15), TokenType.ACCESS_TOKEN);
        String refreshToken = jwtService.generateToken(user, Duration.ofDays(7), TokenType.REFRESH_TOKEN);
        return new AuthResponse(accessToken, refreshToken);
    }
}
