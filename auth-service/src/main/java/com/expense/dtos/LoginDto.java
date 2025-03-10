package com.expense.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginDto(@NotBlank @Pattern(regexp = "^[a-z0-9_]+$",
                                            message = "Lowercase letters, numbers, and underscores only allowed for username")
                       String username,
                       @NotBlank String password) {
}
