package com.expense.services;

import com.expense.dtos.AccessTokenDto;
import com.expense.dtos.UserRequestDto;
import com.expense.dtos.UserResponseDto;

import java.util.UUID;

public interface UserService {
    UserResponseDto createUser(UserRequestDto user);
    AccessTokenDto login(String username, String password);
    UserResponseDto getUser(String username);
    void deleteUser(String userId);
    UserResponseDto addRole(UUID userId, String role);
}
