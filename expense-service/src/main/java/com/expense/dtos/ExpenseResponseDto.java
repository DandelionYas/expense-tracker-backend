package com.expense.dtos;

import java.time.LocalDate;
import java.util.UUID;

public record ExpenseResponseDto(
        UUID id,
        UserDto user,
        double amount,
        String description,
        ExpenseCategoryDto category,
        LocalDate date) {
}
