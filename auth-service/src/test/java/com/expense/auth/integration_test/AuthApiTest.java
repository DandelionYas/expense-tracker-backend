package com.expense.auth.integration_test;

import com.expense.dtos.AccessTokenDto;
import com.expense.dtos.LoginDto;
import com.expense.dtos.UserRequestDto;
import com.expense.dtos.UserResponseDto;
import com.expense.utils.EncryptionUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static com.expense.auth.configs.Constants.BASE_URL;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthApiTest {
    @LocalServerPort
    private int port;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private EncryptionUtils encryptionUtils;
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
        ResponseEntity<AccessTokenDto> entity = restTemplate.exchange(
                BASE_URL.formatted(port, "users/login"),
                HttpMethod.POST, new HttpEntity<>(new LoginDto(username, encryptionUtils.encrypt(password))),
                AccessTokenDto.class);

        assertNotNull(entity.getBody());
        assertNotNull(entity.getBody().accessToken());
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
        ResponseEntity<UserRequestDto> entity = restTemplate.exchange(
                BASE_URL.formatted(port, "users/%s".formatted(username)),
                HttpMethod.GET, new HttpEntity<>(headers), UserRequestDto.class);

        assertNotNull(entity.getBody());
        assertEquals(username, entity.getBody().username());
        assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

    /**
     * Creating user has effect on keycloak
     * So for integration test, I decided to only test existing user
     * Conflict response will come back from keycloak
     * Error will be wrapped to UserNotCreatedException
     * @see com.expense.exceptions.UserNotCreatedException
     */
    @Test
    @Order(3)
    public void testReturningBadRequestWhenCreatingExistingUser() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto(username, "a@b.com", "Yaser", "Ghaderipour", encryptionUtils.encrypt(password));
        try {
            restTemplate.exchange(
                    BASE_URL.formatted(port, "users/signup"),
                    HttpMethod.POST, new HttpEntity<>(userRequestDto),
                    UserRequestDto.class);
        } catch (Exception e) {
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, ((HttpClientErrorException.BadRequest) e).getStatusCode());
        }
    }

    /**
     * Creating a temp user which is going to be deleted in next Test
     */
    @Test
    @Order(4)
    public void testCreatingUserSuccessfully() throws Exception {
        String tempUsername = "temp";
        UserRequestDto userRequestDto = new UserRequestDto(tempUsername, "a@b.com", "Yaser", "Ghaderipour", encryptionUtils.encrypt(password));

        ResponseEntity<UserResponseDto> response = restTemplate.exchange(
                BASE_URL.formatted(port, "users/signup"),
                HttpMethod.POST, new HttpEntity<>(userRequestDto),
                UserResponseDto.class);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        this.tempUserId = response.getBody().id();
        assertEquals(tempUsername, response.getBody().username());
    }

    @Test
    @Order(5)
    public void testAssigningRoleToUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenResponse.getToken());
        UriComponents uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("localhost")
                .port(port)
                .path("/api/users/")
                .path(tempUserId)
                .path("/role")
                .path("/admin").build();
        ResponseEntity<UserResponseDto> response = restTemplate.exchange(
                uri.toString(), HttpMethod.PUT, new HttpEntity<>(headers), UserResponseDto.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Test removing an existing user
     * Order is important specially for deleting the temp user
     */
    @Test
    @Order(6)
    public void testDeletingUserFromKeycloakById() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenResponse.getToken());
        assertDoesNotThrow(() -> restTemplate.exchange(BASE_URL.formatted(port, "users/%s".formatted(tempUserId)),
                HttpMethod.DELETE, new HttpEntity<>(headers), Void.class));
    }
}
