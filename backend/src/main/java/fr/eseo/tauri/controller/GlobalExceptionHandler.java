package fr.eseo.tauri.controller;

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
import org.springframework.web.method.HandlerMethod;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Method to generate the message containing all the necessary information to localize the origin of the exception, its type, and give clues on how to fix it.
     * @param e the exception
     * @param handlerMethod the method that threw the exception
     * @param request the request that caused the exception
     * @return the error message
     */
    private String generateMessage(Exception e, HandlerMethod handlerMethod, HttpServletRequest request) {
        return "An exception has been thrown at " + handlerMethod.getMethod().getDeclaringClass() + ", by " + handlerMethod.getMethod().getName() + " method."  + " \n\nType of exception : " +e.getClass().getCanonicalName() + "\n\nMessage : " + e.getMessage() + "\n\nRequest path : " + request.getServletPath();
    }

    //First we handle all the exceptions related to bad parameters for the requests
    //Pour les exceptions de validation, utiliser BAD REQUEST
    @ExceptionHandler(value = {IllegalArgumentException.class, NumberFormatException.class, ArrayIndexOutOfBoundsException.class,
            ServletRequestBindingException.class, HttpMessageNotReadableException.class, TypeMismatchException.class,
            HandlerMethodValidationException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleBadRequests(Exception e, HandlerMethod handlerMethod, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generateMessage(e, handlerMethod, request));
    }

    //Handle the exception related tou not found elements
    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<Object> handleNullPointerException(NullPointerException e, HandlerMethod handlerMethod, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(generateMessage(e, handlerMethod, request));
    }

    //Handle all other exceptions
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleUnhandledExceptions(Exception e, HandlerMethod handlerMethod, HttpServletRequest request) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(generateMessage(e, handlerMethod, request));
    }
}
