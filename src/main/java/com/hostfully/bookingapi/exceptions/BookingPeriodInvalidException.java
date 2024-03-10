package com.hostfully.bookingapi.exceptions;

public class BookingPeriodInvalidException extends RuntimeException {

    public BookingPeriodInvalidException() {
        super();
    }

    public BookingPeriodInvalidException(String message) {
        super(message);
    }
}
