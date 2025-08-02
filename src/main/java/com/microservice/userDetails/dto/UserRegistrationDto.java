package com.microservice.userDetails.dto;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegistrationDto {
    // @NotBlank is used to ensure that the field is not null and not empty and its equal to @NotEmpty and  @NotNull
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Username can only contain letters, numbers, underscores, periods, and hyphens")
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
}
