package com.expense.controllers;

import com.expense.dtos.UserRecord;
import com.expense.services.UserService;
import jakarta.ws.rs.core.Response;
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

    @PostMapping("/signup")
    public ResponseEntity<Response.StatusType> createUser(@RequestBody UserRecord userRecord) throws Exception {
        Response response = userService.createUser(userRecord);
        return new ResponseEntity<>(response.getStatusInfo(), HttpStatus.valueOf(response.getStatus()));
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
}