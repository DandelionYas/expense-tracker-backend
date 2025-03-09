package com.expense.services;

import com.expense.dtos.UserRecord;
import jakarta.ws.rs.core.Response;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;

public interface UserService {
    Response createUser(UserRecord user) throws Exception;
    AccessTokenResponse login(String username, String password) throws Exception;
    UserRepresentation getUser(String username);
}
