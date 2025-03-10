package com.expense.aop;

import com.expense.dtos.ErrorDto;
import com.expense.exceptions.PasswordDecryptionException;
import com.expense.exceptions.UserNotCreatedException;
import com.expense.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.util.StringUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Exception handler to provide a clean API error output
 * TODO: extract common parts to a common module
 */
@Slf4j
@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFoundException(UserNotFoundException e, HttpServletRequest request) {
        ErrorDto errorDto = new ErrorDto(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "User not found",
                StringUtils.truncate(e.getMessage(), 50),
                request.getRequestURI(),
                null);
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotCreatedException.class)
    public ResponseEntity<ErrorDto> handleUserNotCreatedException(UserNotCreatedException e, HttpServletRequest request) {
        ErrorDto errorDto = new ErrorDto(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "User not created",
                StringUtils.truncate(e.getMessage(), 50),
                request.getRequestURI(),
                null);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordDecryptionException.class)
    public ResponseEntity<ErrorDto> handlePasswordDecryptionException(PasswordDecryptionException e, HttpServletRequest request) {
        ErrorDto errorDto = new ErrorDto(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Password Decrypting Error",
                StringUtils.truncate(e.getMessage(), 50),
                request.getRequestURI(),
                null);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorDto> handleAccessDeniedException(AuthorizationDeniedException e, HttpServletRequest request) {
        ErrorDto errorDto = new ErrorDto(LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Access Denied Error",
                StringUtils.truncate(e.getMessage(), 50),
                request.getRequestURI(),
                null);
        return new ResponseEntity<>(errorDto, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        List<String> errorList = e.getBindingResult().getAllErrors().stream().map(ObjectError::toString).toList();
        ErrorDto errorDto = new ErrorDto(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Binding Validation Error",
                StringUtils.truncate(e.getMessage(), 50),
                request.getRequestURI(),
                errorList);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorDto> handleConstrainValidationException(ValidationException e, HttpServletRequest request) {
        ErrorDto errorDto = new ErrorDto(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Constrain Validation Error",
                StringUtils.truncate(e.getMessage(), 50),
                request.getRequestURI(),
                null);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorDto> handleGeneralException(Throwable e, HttpServletRequest request) {
        ErrorDto errorDto = new ErrorDto(LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error: %s, Cause: %s".formatted(e.getClass().getSimpleName(), e.getCause()),
                StringUtils.truncate(e.getMessage(), 50),
                request.getRequestURI(),
                null);
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
