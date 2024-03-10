package com.hostfully.bookingapi.exceptions.handler;

import com.hostfully.bookingapi.exceptions.BookingPeriodInvalidException;
import com.hostfully.bookingapi.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    ProblemDetail handlerResourceNotFoundException(ResourceNotFoundException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problemDetail.setTitle("Resource Not Found");

        return problemDetail;
    }

    @ExceptionHandler(BookingPeriodInvalidException.class)
    ProblemDetail handlerBookingPeriodInvalidException(BookingPeriodInvalidException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        problemDetail.setTitle("Booking Period Invalid");

        return problemDetail;
    }
}
