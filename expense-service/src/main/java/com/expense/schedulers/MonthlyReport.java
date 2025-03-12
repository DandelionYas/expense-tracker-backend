package com.expense.schedulers;

import com.expense.services.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MonthlyReport {

    private final ExpenseService expenseService;

    /**
     * Generate monthly report in the day first of each month
     * At 10:00:00 AM
     */
    @Scheduled(cron = "0 0 10 1 */1 *")
    public void monthlyReport() {
        // TODO: Generate actual report

        // Sending message to RebbitMQ
    }
}
