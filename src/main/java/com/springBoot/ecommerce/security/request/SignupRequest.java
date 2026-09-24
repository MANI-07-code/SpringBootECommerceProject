package com.springBoot.ecommerce.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SignupRequest {
    @NotBlank
    private String username;
    @Email
    private String email;
    @NotBlank
    @Size(min = 6 , max = 40)
    private String password;
    private Set<String> roles;
}
