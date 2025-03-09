package com.expense.auth.integration_test;

import com.expense.dtos.UserRecord;
import com.expense.utils.EncryptionUtil;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static com.expense.auth.configs.Constants.BASE_URL;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiTest {
    @LocalServerPort
    private int port;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private EncryptionUtil encryptionUtil;
    @Value("${test.username}")
    private String username;
    @Value("${test.password}")
    private String password;
    private AccessTokenResponse tokenResponse;
    private String tempUserId;

    @BeforeEach
    void setUp() {
        // No need to use RestTemplate for getting token from Keycloak
        // Use AuthzClient API from Keycloak community
        tokenResponse = AuthzClient.create().obtainAccessToken(username, password);
    }

    /**
     * Test successful login providing encrypted password
     * @throws Exception in case of any problem
     */
    @Test
    @Order(0)
    public void testSuccessLoginWithoutProvidingAccessToken() throws Exception {
        ResponseEntity<AccessTokenResponse> entity = restTemplate.exchange(
                BASE_URL.formatted(port, "users/login"),
                HttpMethod.POST, new HttpEntity<>(new UserRecord(username, encryptionUtil.encrypt(password))),
                AccessTokenResponse.class);

        assertNotNull(entity.getBody());
        assertNotNull(entity.getBody().getToken());
        assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

    /**
     * Test getting user details from /api/users/{username}
     * Nullity check, Content Check and ResponseStatus Check
     */
    @Test
    @Order(2)
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

    /**
     * Creating user has effect on keycloak
     * So for integration test, I decided to only test existing user
     * Conflict response will come back from keycloak
     */
    @Test
    @Order(3)
    public void testReturningConflictWhenCreatingExistingUser() throws Exception {
        UserRecord userRecord = new UserRecord(username, "a@b.com", "Yaser", "Ghaderipour", encryptionUtil.encrypt(password));
        try {
            ResponseEntity<Response.StatusType> entity = restTemplate.exchange(
                    BASE_URL.formatted(port, "users/signup"),
                    HttpMethod.POST, new HttpEntity<>(userRecord),
                    Response.StatusType.class);
        } catch (Exception e) {
            Assertions.assertEquals(HttpStatus.CONFLICT, ((HttpClientErrorException.Conflict) e).getStatusCode());
        }
    }

    /**
     * Creating a temp user which is going to be deleted in next Test
     */
    @Test
    @Order(4)
    public void testCreatingUserSuccessfully() throws Exception {
        String tempUsername = "temp";
        UserRecord userRecord = new UserRecord(tempUsername, "a@b.com", "Yaser", "Ghaderipour", encryptionUtil.encrypt(password));

        ResponseEntity<UserRepresentation> response = restTemplate.exchange(
                BASE_URL.formatted(port, "users/signup"),
                HttpMethod.POST, new HttpEntity<>(userRecord),
                UserRepresentation.class);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        this.tempUserId = response.getBody().getId();
        assertEquals(response.getBody().getUsername(), tempUsername);
    }

    /**
     * Test removing an existing user
     * Order is important specially for deleting the temp user
     */
    @Test
    @Order(5)
    public void testDeletingUserFromKeycloakById() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenResponse.getToken());
        assertDoesNotThrow(() -> restTemplate.exchange(BASE_URL.formatted(port, "users/%s".formatted(tempUserId)),
                HttpMethod.DELETE, new HttpEntity<>(headers), Void.class));
    }
}
