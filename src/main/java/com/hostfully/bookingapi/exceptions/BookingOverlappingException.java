package com.hostfully.bookingapi.exceptions;

public class BookingOverlappingException extends RuntimeException {

    public BookingOverlappingException() {
        super();
    }

    public BookingOverlappingException(String message) {
        super(message);
    }
}
