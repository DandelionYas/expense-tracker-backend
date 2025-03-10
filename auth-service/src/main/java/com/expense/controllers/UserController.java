package com.expense.controllers;

import com.expense.dtos.LoginDto;
import com.expense.dtos.UserRequestDto;
import com.expense.dtos.UserResponseDto;
import com.expense.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Create user
     *
     * @param userRequestDto contains user's information
     * @return UserRepresentation containing
     * @throws Exception
     */
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserRequestDto userRequestDto) throws Exception {
        return new ResponseEntity<>(userService.createUser(userRequestDto), HttpStatus.CREATED);
    }

    /**
     * A public API to grant users and generated jwt token
     *
     * @param loginDto user's credentials
     * @return AccessToken
     * @throws Exception
     */
    @PostMapping("/login")
    public AccessTokenResponse login(@RequestBody @Valid LoginDto loginDto) throws Exception {
        return userService.login(loginDto.username(), loginDto.password());
    }

    /**
     * Fetch User Details
     *
     * @param username search based on this parameter
     * @return UserRepresentation object containing User Details
     */
    @GetMapping("/{username}")
    public UserResponseDto getUser(@PathVariable(value = "username")
                                   @Pattern(regexp = "^[a-z]+$") String username) {
        return userService.getUser(username);
    }

    /**
     * Removing a user by id
     *
     * @param userId user's id
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable(value = "id") String userId) {
        userService.deleteUser(userId);
    }
}