package com.expense.services.impl;

import com.expense.configs.KeycloakProperties;
import com.expense.exceptions.UserNotFoundException;
import com.expense.services.UserService;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakUserService implements UserService {

    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProperties;

    /**
     * Connect to Keycloak and find a user by the input username phrase
     * @param username input username of the user we want to find
     * @return UserRepresentation object from Keycloak
     */
    public UserRepresentation getUser(String username) {
        // Search for exact username phrase to return one user
        boolean exactSearchPhrase = true;
        List<UserRepresentation> userRepresentations = keycloak.realm(keycloakProperties.getRealm())
                .users()
                .search(username, exactSearchPhrase);

        if (userRepresentations.isEmpty()) {
            throw new UserNotFoundException(username);
        }

        return userRepresentations.getFirst();
    }
}