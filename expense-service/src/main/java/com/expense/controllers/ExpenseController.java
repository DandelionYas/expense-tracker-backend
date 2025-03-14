package com.expense.controllers;

import com.expense.dtos.ExpenseDto;
import com.expense.dtos.ExpenseResponseDto;
import com.expense.services.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseResponseDto> createExpense(@RequestBody @Valid ExpenseDto expenseDto) {
        return new ResponseEntity<>(expenseService.createExpense(expenseDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{expenseId}")
    public void deleteExpense(@PathVariable("expenseId") UUID expenseId) {
        expenseService.deleteExpense(expenseId);
    }

    @GetMapping
    public Page<ExpenseResponseDto> getExpensesByUser(@RequestParam("userId") UUID userId, Pageable pageable) {
        return expenseService.getExpensesByUser(userId, pageable);
    }

    // TODO: Implement All required APIs
}
