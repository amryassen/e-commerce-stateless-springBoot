package com.microservice.userDetails.service;

import com.microservice.userDetails.dto.UserDto;

public interface AuthService {

    AuthResponse register(UserDto userDto);
    AuthResponse login(UserDto userDto);
}
