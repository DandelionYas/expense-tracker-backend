package com.expense.controllers;

import com.expense.dtos.UserCredentials;
import com.expense.services.UserService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * A public API to grant users and generated jwt token
     * @param userCredentials user's credentials
     * @return AccessToken
     * @throws Exception
     */
    @PostMapping("/login")
    public AccessTokenResponse login(@RequestBody UserCredentials userCredentials) throws Exception {
        return userService.login(userCredentials.username(), userCredentials.password());
    }

    /**
     * Fetch User Details
     * @param username search based on this parameter
     * @return UserRepresentation object containing User Details
     */
    @GetMapping("/{username}")
    public UserRepresentation getUser(@PathVariable("username") String username) {
        return userService.getUser(username);
    }
}