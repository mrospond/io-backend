package com.example.iobackend.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String TIMESTAMP = "timestamp";
    private static final String MESSAGE = "message";
    private static final String STATUS = "status";
    private static final String ERRORS = "errors";

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<Object> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException exception) {
        Map<String, Object> map = new HashMap<>();
        map.put(TIMESTAMP, LocalDateTime.now());
        map.put(MESSAGE, exception.getMessage());

        log.error("{}: ", exception.getClass().getSimpleName(), exception);
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException exception) {
        Map<String, Object> map = new HashMap<>();
        map.put(TIMESTAMP, LocalDateTime.now());
        map.put(MESSAGE, exception.getMessage());
        log.error("{}: ", exception.getClass().getSimpleName(), exception);
        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException exception) {
        Map<String, Object> map = new HashMap<>();
        map.put(TIMESTAMP, LocalDateTime.now());
        map.put(MESSAGE, exception.getMessage());
        log.error("{}: ", exception.getClass().getSimpleName(), exception);
        return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(STATUS, status.value());

        List<String> errors = exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        body.put(ERRORS, errors);

        log.error("{}: ", exception.getClass().getSimpleName(), exception);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationFailure(AuthenticationException exception) {
        Map<String, Object> map = new HashMap<>();
        map.put(TIMESTAMP, LocalDateTime.now());
        map.put(MESSAGE, "Failed to log in");

        log.error("{}: ", exception.getClass().getSimpleName(), exception);
        return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JsoupConnectionException.class)
    public ResponseEntity<Object> handleJsoupConnectionException(JsoupConnectionException exception) {
        Map<String, Object> map = new HashMap<>();
        map.put(TIMESTAMP, LocalDateTime.now());
        map.put(MESSAGE, "Jsoup connection error occurred");

        log.error("{}: ", exception.getClass().getSimpleName(), exception);
        return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
    }
}
