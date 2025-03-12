package com.expense.integration_test;

import com.expense.configs.TestsConfiguration;
import com.expense.dtos.ExpenseCategoryDto;
import com.expense.dtos.ExpenseDto;
import com.expense.dtos.ExpenseResponseDto;
import com.expense.dtos.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.expense.configs.Constants.BASE_URL;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestsConfiguration.class)
public class ExpenseApiTest {

    @LocalServerPort
    private int port;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${test.user.id}")
    private UUID userId;
    private UUID expenseId;

    @Test
    @Order(1)
    public void testSavingExpenseSuccessfully() {
        ExpenseDto expenseDto = new ExpenseDto(new UserDto(userId),
                200_000, "Pizza", new ExpenseCategoryDto("Food"), LocalDate.now());

        ResponseEntity<ExpenseResponseDto> response = restTemplate.exchange(
                BASE_URL.formatted(port, "expenses"),
                HttpMethod.POST, new HttpEntity<>(expenseDto),
                ExpenseResponseDto.class);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        expenseId = response.getBody().id();
    }

    @Test
    @Order(2)
    public void testRemovingExpenseSuccessfully() {
        testSavingExpenseSuccessfully();
        assertDoesNotThrow(() -> restTemplate.exchange(BASE_URL.formatted(port, "expenses/%s".formatted(expenseId)),
                HttpMethod.DELETE, null, Void.class));
    }

    @Test
    @Order(3)
    public void testGettingExpensesForUserSuccessfully() {
        // TODO: use UriComponents for all tests
        UriComponents uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(port)
                .path("/api/expenses")
                .queryParam("userId", userId)
                .queryParam("size", 10)
                .queryParam("number", 0).build();

        ResponseEntity<?> response = restTemplate.exchange(uri.toString(),
                HttpMethod.GET, null, HelperPage.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
