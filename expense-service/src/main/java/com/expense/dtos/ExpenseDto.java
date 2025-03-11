package com.expense.dtos;

import java.time.LocalDate;

public record ExpenseDto(
        UserDto user,
        double amount,
        String description,
        ExpenseCategoryDto category,
        LocalDate date) {
}
