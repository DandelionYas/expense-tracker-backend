package com.expense.schedulers;

import com.expense.publishers.RabbitMqPublisher;
import com.expense.services.ExpenseService;
import com.nimbusds.jose.shaded.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class MonthlyReport {

    private final ExpenseService expenseService;
    private final RabbitMqPublisher rabbitMqPublisher;

    /**
     * Generate monthly report in the day first of each month
     * At 10:00:00 AM
     */
    @Scheduled(cron = "0 0 10 1 */1 *")
    public void monthlyReport() {
        log.info("Monthly report triggered");
        LocalDate start = LocalDate.now();
        // TODO: Generate actual report

        // Sending message to RabbitMQ
        JsonObject alertMessage = new JsonObject();
        alertMessage.addProperty("alert", "You spent too much on coffee!");
        rabbitMqPublisher.send(alertMessage);
        LocalDate end = LocalDate.now();
        log.info("Monthly report completed. duration: {}ms", Duration.between(start, end).toMillis());
    }
}
