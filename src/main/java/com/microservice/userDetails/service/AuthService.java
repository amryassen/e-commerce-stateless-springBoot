package com.microservice.userDetails.service;

import com.microservice.userDetails.dto.AuthResponse;
import com.microservice.userDetails.dto.UserDto;
import com.microservice.userDetails.dto.UserLoginDto;
import com.microservice.userDetails.dto.UserRegistrationDto;

public interface AuthService {
    AuthResponse register(UserRegistrationDto userRegistrationDto);
    AuthResponse login(UserLoginDto userLoginDto);
}
