package com.expense.services;

import com.expense.dtos.AccessTokenDto;
import com.expense.dtos.UserRequestDto;
import com.expense.dtos.UserResponseDto;

public interface UserService {
    UserResponseDto createUser(UserRequestDto user);
    AccessTokenDto login(String username, String password);
    UserResponseDto getUser(String username);
    void deleteUser(String userId);
}
