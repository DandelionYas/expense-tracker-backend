package com.expense.services.impl;

import com.expense.configs.KeycloakProperties;
import com.expense.dtos.UserRequestDto;
import com.expense.dtos.UserResponseDto;
import com.expense.exceptions.UserNotCreatedException;
import com.expense.exceptions.UserNotFoundException;
import com.expense.mappers.UserMapper;
import com.expense.services.UserService;
import com.expense.utils.EncryptionUtils;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakUserService implements UserService {

    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProperties;
    private final EncryptionUtils encryptionUtils;

    /**
     * Create user by calling Keycloak API
     *
     * @param user user's information
     * @return Response object containing response status
     * @throws Exception in case of password decryption or API call issue
     */
    @Override
    public UserResponseDto createUser(UserRequestDto user) throws Exception {
        UserRepresentation keycloakUser = UserMapper.INSTANCE.dtoToEntity(user);
        keycloakUser.setEnabled(true);
        keycloakUser.setEmailVerified(false);

        if (keycloakUser.getCredentials()!= null && keycloakUser.getCredentials().isEmpty()) {
            CredentialRepresentation credential = keycloakUser.getCredentials().get(0);
            String plainPassword = encryptionUtils.decrypt(credential.getValue());
            credential.setValue(plainPassword);
        }

        Response keycloakResponse = getUsersResource().create(keycloakUser);
        if (HttpStatus.CREATED.value() != keycloakResponse.getStatus()) {
            throw new UserNotCreatedException(keycloakResponse.getStatusInfo().getReasonPhrase());
        }

        return getUser(user.username());
    }

    /**
     * Decrypt password and refer to Keycloak for AccessToken
     *
     * @param username user's username
     * @param password user's encrypted password
     * @return AccessToken
     * todo: exception handling
     * @throws Exception in case of decrypt or get token issues
     */
    @Override
    public AccessTokenResponse login(String username, String password) throws Exception {
        String plainPassword = encryptionUtils.decrypt(password);
        return AuthzClient.create().obtainAccessToken(username, plainPassword);
    }

    /**
     * Connect to Keycloak and find a user by the input username phrase
     *
     * @param username input username of the user we want to find
     * @return UserRepresentation object from Keycloak
     */
    public UserResponseDto getUser(String username) {
        // Search for exact username phrase to return one user
        boolean exactSearchPhrase = true;
        List<UserRepresentation> userRepresentations = getUsersResource()
                .search(username, exactSearchPhrase);

        if (userRepresentations.isEmpty()) {
            throw new UserNotFoundException("Unable to find user for the username: %s".formatted(username));
        }

        return UserMapper.INSTANCE.entityToDto(userRepresentations.getFirst());
    }

    @Override
    public void deleteUser(String userId) {
        getUsersResource().delete(userId);
    }

    /**
     * User management can be done by APIs provided in UsersResource
     * @return UsersResource
     */
    private UsersResource getUsersResource() {
        return keycloak
                .realm(keycloakProperties.getRealm())
                .users();
    }
}