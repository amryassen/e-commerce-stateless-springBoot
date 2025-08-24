package com.microservice.userDetails.validation;

import com.microservice.userDetails.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.annotation.Annotation;

public class UniqueUserNameValidator implements ConstraintValidator<UniqueUserName,String> {

    private UserRepository userRepository;

    @Override
    public void initialize(UniqueUserName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String userName, ConstraintValidatorContext context) {
        return !userRepository.existsByUsername(userName);
    }
}
