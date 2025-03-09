package com.expense.services.impl;

import com.expense.configs.KeycloakProperties;
import com.expense.dtos.UserCreationResponse;
import com.expense.dtos.UserRecord;
import com.expense.exceptions.UserNotFoundException;
import com.expense.services.UserService;
import com.expense.utils.EncryptionUtil;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakUserService implements UserService {

    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProperties;
    private final EncryptionUtil encryptionUtil;

    /**
     * Create user by calling Keycloak API
     * @param user user's information
     * @return Response object containing response status
     * @throws Exception in case of password decryption or API call issue
     */
    @Override
    public UserCreationResponse createUser(UserRecord user) throws Exception {
        UserRepresentation keycloakUser = new UserRepresentation();
        keycloakUser.setUsername(user.username());
        keycloakUser.setFirstName(user.firstName());
        keycloakUser.setLastName(user.lastName());
        keycloakUser.setEmail(user.email());
        keycloakUser.setEnabled(true);
        keycloakUser.setEmailVerified(false);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setTemporary(false);
        String plainPassword = encryptionUtil.decrypt(user.password());
        credential.setValue(plainPassword);
        keycloakUser.setCredentials(List.of(credential));

        Response keycloakResponse = getUsersResource().create(keycloakUser);
        return new UserCreationResponse(keycloakResponse.getStatusInfo(), getUser(user.username()));
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
        String plainPassword = encryptionUtil.decrypt(password);
        return AuthzClient.create().obtainAccessToken(username, plainPassword);
    }

    /**
     * Connect to Keycloak and find a user by the input username phrase
     * @param username input username of the user we want to find
     * @return UserRepresentation object from Keycloak
     */
    public UserRepresentation getUser(String username) {
        // Search for exact username phrase to return one user
        boolean exactSearchPhrase = true;
        List<UserRepresentation> userRepresentations = getUsersResource()
                .search(username, exactSearchPhrase);

        if (userRepresentations.isEmpty()) {
            throw new UserNotFoundException(username);
        }

        return userRepresentations.getFirst();
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