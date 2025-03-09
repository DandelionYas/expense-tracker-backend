package com.expense.controllers;

import com.expense.dtos.UserCreationResponse;
import com.expense.dtos.UserRecord;
import com.expense.services.UserService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Create user
     * @param userRecord contains user's information
     * @return UserRepresentation containing
     * @throws Exception
     */
    @PostMapping("/signup")
    public ResponseEntity<UserRepresentation> createUser(@RequestBody UserRecord userRecord) throws Exception {
        UserCreationResponse response = userService.createUser(userRecord);
        return new ResponseEntity<>(response.user(), HttpStatus.valueOf(response.statusType().getStatusCode()));
    }

    /**
     * A public API to grant users and generated jwt token
     * @param userRecord user's credentials
     * @return AccessToken
     * @throws Exception
     */
    @PostMapping("/login")
    public AccessTokenResponse login(@RequestBody UserRecord userRecord) throws Exception {
        return userService.login(userRecord.username(), userRecord.password());
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

    /**
     * Removing a user by id
     * @param userId user's id
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") String userId) {
        userService.deleteUser(userId);
    }
}