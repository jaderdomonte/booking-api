package com.hostfully.bookingapi.domain;

import com.hostfully.bookingapi.exceptions.DomainObjectValidationException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Booking {

    private Long id;

    private Guest guest;

    private BookingPeriodVO period;

    private BookingStatusVO status;

    public Booking(Long id, Guest guest, BookingPeriodVO period, BookingStatusVO status) {
        this.id = id;
        this.guest = guest;
        this.period = period;
        this.status = status;
        validate();
    }

    public Booking(Guest guest, BookingPeriodVO period) {
        this.guest = guest;
        this.period = period;
        validate();
    }

    private void validate(){
        if(guest == null){
            throw new DomainObjectValidationException("Guest is required.");
        }

        if(period == null){
            throw new DomainObjectValidationException("Period is required.");
        }

        if(status == null){
            throw new DomainObjectValidationException("Status is required.");
        }
    }
}
