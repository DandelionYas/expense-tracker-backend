package com.expense.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDto(@NotBlank(message = "Username is mandatory")
                             String username,
                             @Email
                             String email,
                             String firstName,
                             String lastName,
                             String password) {
}
