//package com.example.ewallet.exceptions;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.context.request.WebRequest;
//
//import java.util.Date;
//
//@ControllerAdvice
//public class CustomizedResponseEntityExceptionHandler {
//
//    @ExceptionHandler(Exception.class)
//    public final ResponseEntity<Object> somethingWentWrong(Exception ex, WebRequest request) {
//        ErrorMessage exceptionResponse = new ErrorMessage(ex.getMessage(), ex, new Date(), request.getDescription(false));
//        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(FieldValidationException.class)
//    public final ResponseEntity<Object> handleFieldValidationException(FieldValidationException ex, WebRequest request) {
//        ErrorMessage exceptionResponse = new ErrorMessage(ex.getMessage(), ex, new Date(), request.getDescription(false));
//        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
//    }
//}
