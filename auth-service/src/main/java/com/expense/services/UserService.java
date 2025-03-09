package com.expense.services;

import com.expense.dtos.UserCreationResponse;
import com.expense.dtos.UserRecord;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;

public interface UserService {
    UserCreationResponse createUser(UserRecord user) throws Exception;
    AccessTokenResponse login(String username, String password) throws Exception;
    UserRepresentation getUser(String username);
}
