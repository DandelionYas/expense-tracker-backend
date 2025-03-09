package com.expense.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRequestDto(String id,
                             @NotBlank(message = "Username is mandatory") String username,
                             // OWASP email validation
                             @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", message = "Invalid email")
                             String email,
                             String firstName,
                             String lastName,
                             String password) {

    public UserRequestDto(String username, String email, String firstName, String lastName, String password) {
        this(null, username, email, firstName, lastName, password);
    }
}
