package com.hostfully.bookingapi.domain;

import com.hostfully.bookingapi.exceptions.BookingPeriodInvalidException;
import com.hostfully.bookingapi.exceptions.DomainObjectValidationException;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class BookingPeriodVO {

    private LocalDate checkIn;

    private LocalDate checkOut;

    public BookingPeriodVO(LocalDate checkIn, LocalDate checkOut) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        validate();
    }

    private void validate(){
        if(checkIn == null || checkOut == null){
            throw new DomainObjectValidationException("CheckIn and CheckOut are required.");
        }

        if(checkIn.isEqual(checkOut) || checkIn.isAfter(checkOut)){
            throw new BookingPeriodInvalidException("CheckOut date should be greater than CheckIn date.");
        }
    }
}
