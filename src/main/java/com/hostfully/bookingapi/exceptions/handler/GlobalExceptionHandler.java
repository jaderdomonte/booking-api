package com.hostfully.bookingapi.exceptions.handler;

import com.hostfully.bookingapi.exceptions.BookingOverlappingException;
import com.hostfully.bookingapi.exceptions.BookingPeriodInvalidException;
import com.hostfully.bookingapi.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    ProblemDetail handlerResourceNotFoundException(ResourceNotFoundException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problemDetail.setTitle("Resource not found.");

        return problemDetail;
    }

    @ExceptionHandler(BookingPeriodInvalidException.class)
    ProblemDetail handlerBookingPeriodInvalidException(BookingPeriodInvalidException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        problemDetail.setTitle("Booking period is invalid.");

        return problemDetail;
    }

    @ExceptionHandler(DateTimeParseException.class)
    ProblemDetail handlerDateTimeParseException(DateTimeParseException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        problemDetail.setTitle("Some Date value is invalid.");

        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "There are some validation errors");
        problemDetail.setTitle("Bad Request");
        problemDetail.setProperty("Validation Errors", getErrors(exception));

        return problemDetail;
    }

    private Map<String, List<String>> getErrors(MethodArgumentNotValidException exception) {
        Map<String, List<String>> errors = exception.getBindingResult().getFieldErrors()
                                                        .stream()
                                                        .collect(Collectors.groupingBy(FieldError::getField,
                                                                Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())));
        return errors;
    }

    @ExceptionHandler(BookingOverlappingException.class)
    ProblemDetail handlerBookingOverlappingException(BookingOverlappingException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "There is a overlapping occurency");
        problemDetail.setTitle("Bad Request");

        return problemDetail;
    }


}
