package com.expense.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRequestDto(@NotBlank(message = "Username is mandatory")
                             @Pattern(regexp = "^[a-z]+$", message = "Lowercase only allowed for username")
                             String username,
                             @Email
                             String email,
                             String firstName,
                             String lastName,
                             String password) {
}
