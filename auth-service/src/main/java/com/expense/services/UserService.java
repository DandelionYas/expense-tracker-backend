package com.expense.services;

import com.expense.dtos.UserRequestDto;
import com.expense.dtos.UserResponseDto;
import org.keycloak.representations.AccessTokenResponse;

public interface UserService {
    UserResponseDto createUser(UserRequestDto user);
    AccessTokenResponse login(String username, String password);
    UserResponseDto getUser(String username);
    void deleteUser(String userId);
}
