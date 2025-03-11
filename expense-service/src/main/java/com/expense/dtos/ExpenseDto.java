package com.expense.dtos;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ExpenseDto(
        @NotNull
        UserDto user,
        double amount,
        String description,
        @NotNull
        ExpenseCategoryDto category,
        LocalDate date) {
}
