package com.expense.services;

import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;

public interface UserService {
    UserRepresentation getUser(String username);
    AccessTokenResponse login(String username, String password) throws Exception;
}
