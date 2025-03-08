package com.expense.services;

import org.keycloak.representations.idm.UserRepresentation;

public interface UserService {
    UserRepresentation getUser(String username);
}
