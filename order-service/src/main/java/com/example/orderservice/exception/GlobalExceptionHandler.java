package com.example.orderservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	 @ExceptionHandler(RuntimeException.class)
	    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
		   ErrorResponse response = new ErrorResponse(
	                ex.getMessage(),
	                HttpStatus.BAD_REQUEST.value()
	        );
		   
		   return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }
}
