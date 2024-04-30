package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.exception.ExceptionResponse;
import fr.eseo.tauri.model.exception.ResourceNotFoundException;
import fr.eseo.tauri.util.CustomLogger;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    public static final String UNAUTHORIZED_ACTION = "Unauthorized action";

    //Handle the exceptions related to bad parameters for the requests
    //Pour les exceptions de validation, utiliser BAD REQUEST
    @ExceptionHandler(value = {IllegalArgumentException.class, NumberFormatException.class, ArrayIndexOutOfBoundsException.class,
            ServletRequestBindingException.class, HttpMessageNotReadableException.class, TypeMismatchException.class,
            HandlerMethodValidationException.class})
    public ResponseEntity<ExceptionResponse> handleBadRequestException(Exception e, HttpServletRequest request) {
        CustomLogger.logInfo(String.valueOf(new ExceptionResponse(e, request)));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(e,  request));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    //Handle the exceptions related to not found elements
    @ExceptionHandler(value = {NullPointerException.class, ResourceNotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleNotFoundException(Exception e, HttpServletRequest request) {
        CustomLogger.logInfo(String.valueOf(new ExceptionResponse(e, request)));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(e, request));
    }

    //Handle the exceptions related to unauthorized actions
    @ExceptionHandler(value = {SecurityException.class})
    public ResponseEntity<ExceptionResponse> handleUnauthorizedException(Exception e, HttpServletRequest request) {
        CustomLogger.logInfo(String.valueOf(new ExceptionResponse(e, request)));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionResponse(e, request));
    }

    //Handle all other exceptions
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ExceptionResponse> handleUnhandledExceptions(Exception e, HttpServletRequest request) {
        CustomLogger.logInfo(String.valueOf(new ExceptionResponse(e, request)));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(e, request));
    }
}
