package com.microservice.userDetails.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//@Documented is used to indicate that this annotation should be documented by the Javadoc tool or Swagger.
//@Target specifies the kinds of program elements to which an annotation type is applicable.
//@Retention specifies how long annotations with the annotated type are to be retained. RUNTIME means the annotation will be available at runtime, allowing it to be read reflectively.
@Documented
@Target({java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueEmail {

    String message() default "Email is already in use";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
