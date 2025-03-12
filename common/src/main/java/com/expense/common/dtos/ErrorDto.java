package com.expense.common.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorDto(LocalDateTime timestamp,
                       int status,
                       String error,
                       String message,
                       String path,
                       List<String> validationErrors) {
}
