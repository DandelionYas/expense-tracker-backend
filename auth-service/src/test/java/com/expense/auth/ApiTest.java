package com.expense.auth;

import com.expense.dtos.LoginDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiTest {
    private static final String BASE_URL = "http://localhost:%s/api/%s";

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${test.username}")
    private String username;

    @Value("${test.password}")
    private String password;

    private AccessTokenResponse tokenResponse;

    @BeforeEach
    void setUp() {
        // No need to use RestTemplate for getting token from Keycloak
        // Use AuthzClient API from Keycloak community
        this.tokenResponse = AuthzClient.create().obtainAccessToken(username, password);
    }

    /**
     * Test getting user details from /api/users/{username}
     * Nullity check, Content Check and ResponseStatus Check
     */
    @Test
    public void testGettingUserFromKeycloakByUsername() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenResponse.getToken());
        ResponseEntity<UserRepresentation> entity = restTemplate.exchange(
                BASE_URL.formatted(port, "users/%s".formatted(username)),
                HttpMethod.GET, new HttpEntity<>(headers), UserRepresentation.class);

        assertNotNull(entity.getBody());
        assertEquals(username, entity.getBody().getUsername());
        assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

    @Test
    public void testSuccessLoginWithoutProvidingAccessToken() {
        ResponseEntity<AccessTokenResponse> entity = restTemplate.exchange(
                BASE_URL.formatted(port, "users/login"),
                HttpMethod.POST, new HttpEntity<>(new LoginDto(username, password)),
                AccessTokenResponse.class);

        assertNotNull(entity.getBody());
        assertNotNull(entity.getBody().getToken());
        assertEquals(HttpStatus.OK, entity.getStatusCode());
    }
}
