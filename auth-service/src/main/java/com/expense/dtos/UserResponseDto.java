package com.expense.dtos;

public record UserResponseDto(String id,
                              String username,
                              String email,
                              String firstName,
                              String lastName,
                              String password) {
}
