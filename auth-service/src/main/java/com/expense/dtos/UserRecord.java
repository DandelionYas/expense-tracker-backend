package com.expense.dtos;

public record UserRecord(String username, String email, String firstName, String lastName, String password) {
    public UserRecord(String username, String password) {
        this(username, null, null, null, password);
    }
}
