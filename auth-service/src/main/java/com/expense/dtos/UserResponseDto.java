package com.expense.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponseDto(String id,
                              String username,
                              String email,
                              String firstName,
                              String lastName,
                              String password) {
}
