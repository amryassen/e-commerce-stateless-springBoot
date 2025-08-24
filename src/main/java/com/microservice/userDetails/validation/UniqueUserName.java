package com.microservice.userDetails.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


//@Documented is used to indicate that this annotation should be documented by the Javadoc tool or Swagger.
//@Target specifies the kinds of program elements to which an annotation type is applicable.
//@Retention specifies how long annotations with the annotated type are to be retained. RUNTIME means the annotation will be available at runtime, allowing it to be read reflectively.
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueUserNameValidator.class)
@Target(value = ElementType.FIELD)
public @interface UniqueUserName {
    String message() default "User name is already in use";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
