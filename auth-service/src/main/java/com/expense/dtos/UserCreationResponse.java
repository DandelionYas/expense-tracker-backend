package com.expense.dtos;

import jakarta.ws.rs.core.Response;
import org.keycloak.representations.idm.UserRepresentation;

public record UserCreationResponse(Response.StatusType statusType, UserRepresentation user) {
}
