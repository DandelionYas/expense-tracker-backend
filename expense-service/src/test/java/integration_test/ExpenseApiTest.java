package integration_test;

import com.expense.dtos.ExpenseCategoryDto;
import com.expense.dtos.ExpenseDto;
import com.expense.dtos.ExpenseResponseDto;
import com.expense.dtos.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.UUID;

import static configs.Constants.BASE_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExpenseApiTest {

    @LocalServerPort
    private int port;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${test.user.id}")
    private UUID userId;

    @Test
    public void testSavingExpenseSuccessfully() {
        ExpenseDto expenseDto = new ExpenseDto(new UserDto(userId),
                200_000, "Pizza", new ExpenseCategoryDto("Food"), LocalDate.now());

        ResponseEntity<ExpenseResponseDto> response = restTemplate.exchange(
                BASE_URL.formatted(port, "expenses"),
                HttpMethod.POST, new HttpEntity<>(expenseDto),
                ExpenseResponseDto.class);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}
