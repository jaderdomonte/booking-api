package com.hostfully.bookingapi.domain;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class BookingPeriodVO extends ValidatedDomain {

    private LocalDate checkIn;

    private LocalDate checkOut;

    public BookingPeriodVO(LocalDate checkIn, LocalDate checkOut) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        validateAllFields();
    }

    private void validateAllFields(){
        validateField(checkIn == null || checkOut == null, "CheckIn and CheckOut are required.");
        validateField(checkIn.isEqual(checkOut) || checkIn.isAfter(checkOut), "CheckOut date should be greater than CheckIn date.");
    }
}
