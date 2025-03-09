package com.expense.dtos;

public record AccessTokenDto(String accessToken, String expiresIn, String refreshToken) {
}
