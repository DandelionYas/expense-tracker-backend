package com.expense.controllers;

import com.expense.dtos.AccessTokenDto;
import com.expense.dtos.LoginDto;
import com.expense.dtos.UserRequestDto;
import com.expense.dtos.UserResponseDto;
import com.expense.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    /**
     * Create user
     *
     * @param userRequestDto contains user's information
     * @return UserRepresentation containing
     * @throws Exception
     */
    @Operation(summary = "Crate New User", description = "REST endpoint that creates a user")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "User Created"),
            @APIResponse(responseCode = "400", description = "Validation Error or Password Decryption Error")})
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        return new ResponseEntity<>(userService.createUser(userRequestDto), HttpStatus.CREATED);
    }


    private final UserService userService;

    /**
     * A public API to grant users and generated jwt token
     *
     * @param loginDto user's credentials
     * @return AccessToken
     * @throws Exception
     */
    @Operation(summary = "Login User", description = "REST endpoint that provides access token for a user")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "User Logged In"),
            @APIResponse(responseCode = "400", description = "Validation Error or Password Decryption Error")})
    @PostMapping("/login")
    public AccessTokenDto login(@RequestBody @Valid LoginDto loginDto) {
        return userService.login(loginDto.username(), loginDto.password());
    }

    /**
     * Fetch User Details
     *
     * @param username search based on this parameter
     * @return UserRepresentation object containing User Details
     */
    @Operation(summary = "Get User", description = "REST endpoint that returns user's details")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "User Found and returned"),
            @APIResponse(responseCode = "404", description = "User Not Found"),
            @APIResponse(responseCode = "400", description = "Validation Error")})
    @GetMapping("/{username}")
    public UserResponseDto getUser(@PathVariable(value = "username")
                                   @Pattern(regexp = "^[a-z]+$", message = "Lowercase only allowed for username")
                                   String username) {
        return userService.getUser(username);
    }

    /**
     * Removing a user by id
     *
     * @param userId user's id
     */
    @Operation(summary = "Delete User", description = "REST endpoint that deletes a user")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "User Deleted")})
    // TODO: Define Roles and Only allow Admin to do this
    // TODO: Check if User exists and return not found
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable(value = "id") String userId) {
        userService.deleteUser(userId);
    }

    // TODO: Implement Add Role API
}