package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.exception.ExceptionResponse;
import fr.eseo.tauri.model.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.FileNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    //Handle the exceptions related to bad parameters for the requests
    //Pour les exceptions de validation, utiliser BAD REQUEST
    @ExceptionHandler(value = {IllegalArgumentException.class, NumberFormatException.class, ArrayIndexOutOfBoundsException.class,
            ServletRequestBindingException.class, HttpMessageNotReadableException.class, TypeMismatchException.class,
            HandlerMethodValidationException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ExceptionResponse> handleBadRequestException(Exception e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(e,  request));
    }

    //Handle the exceptions related to not found elements
    @ExceptionHandler(value = {NullPointerException.class, ResourceNotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleNotFoundException(Exception e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(e, request));
    }

    //Handle the exceptions related to unauthorized actions
    @ExceptionHandler(value = {SecurityException.class})
    public ResponseEntity<ExceptionResponse> handleUnauthorizedException(Exception e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionResponse(e, request));
    }

    //Handle all other exceptions
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ExceptionResponse> handleUnhandledExceptions(Exception e, HttpServletRequest request) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(e, request));
    }
}
