package com.expense.controllers;

import com.expense.services.UserService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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