package com.example.taskmanager.utils.exceptions.handler;

import com.example.taskmanager.utils.exceptions.model.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> genericException(Exception ex) {
        ExceptionResponse exception = ExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.name())
                .code(HttpStatus.BAD_REQUEST.value())
                .errors(List.of(ex.getMessage()))
                .build();

        return ResponseEntity.badRequest().body(exception);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> argumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errorList = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    return error.getCode() + ": " + error.getDefaultMessage();
                })
                .toList();

        ExceptionResponse exception = ExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.name())
                .code(HttpStatus.BAD_REQUEST.value())
                .errors(errorList)
                .build();

        return ResponseEntity.badRequest().body(exception);
    }
}
