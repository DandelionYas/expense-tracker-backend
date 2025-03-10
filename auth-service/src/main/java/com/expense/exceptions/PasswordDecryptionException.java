package com.expense.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PasswordDecryptionException extends RuntimeException {
    public PasswordDecryptionException() {
    }

    public PasswordDecryptionException(String message) {
        super(message);
    }

    public PasswordDecryptionException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordDecryptionException(Throwable cause) {
        super(cause);
    }

    public PasswordDecryptionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
