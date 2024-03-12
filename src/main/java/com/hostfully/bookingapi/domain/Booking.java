package com.hostfully.bookingapi.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Booking extends ValidatedDomain {

    private Long id;

    private Guest guest;

    private BookingPeriodVO period;

    private Property property;

    private BookingStatusVO status;

    public Booking(Long id, Guest guest, Property property, BookingPeriodVO period, BookingStatusVO status) {
        this.id = id;
        this.guest = guest;
        this.property = property;
        this.period = period;
        this.status = status;
        validate();
    }

    public Booking(Guest guest, Property property, BookingPeriodVO period, BookingStatusVO status) {
        this.guest = guest;
        this.property = property;
        this.period = period;
        this.status = status;
        validate();
    }

    public Booking(Guest guest, BookingPeriodVO period) {
        this.guest = guest;
        this.period = period;
        validateGuestAndPeriod();
    }

    private void validateGuestAndPeriod(){
        validateField(guest == null, "Guest is required.");
        validateField(period == null, "Period is required.");
    }

    private void validatePropertyAndStatus(){
        validateField(property == null, "Property is required.");
        validateField(status == null, "Status is required.");
    }

    protected void validate(){
        validatePropertyAndStatus();
        validateGuestAndPeriod();
    }
}
