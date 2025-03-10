package com.expense.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginDto(@NotBlank @Pattern(regexp = "^[a-z]+$", message = "only lowercase allowed for username")
                       String username,
                       @NotBlank String password) {
}
