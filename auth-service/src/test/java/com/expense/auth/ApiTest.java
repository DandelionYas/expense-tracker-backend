package com.expense.auth;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiTest {
    private static final String BASE_URL = "http://localhost:%s/api/%s";

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testGettingUserFromKeycloakByUsername() {
        ResponseEntity<List> entity = this.restTemplate.getForEntity(
                BASE_URL.formatted(port, "/users/testuser"), List.class);
        BDDAssertions.then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
