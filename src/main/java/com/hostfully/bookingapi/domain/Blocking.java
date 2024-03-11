package com.hostfully.bookingapi.domain;

import com.hostfully.bookingapi.exceptions.DomainObjectValidationException;
import com.hostfully.bookingapi.infrastructure.db.entity.BookingPeriod;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Blocking extends ValidatedDomain {

    private Long id;

    private Property property;

    private BookingPeriod period;

    public Blocking(Long id, Property property, BookingPeriod period) {
        this(property, period);
        this.id = id;
        validatePropertyAndPeriod();
    }

    public Blocking(Property property, BookingPeriod period) {
        this.property = property;
        this.period = period;
        validatePropertyAndPeriod();
    }

    public Blocking(BookingPeriod period) {
        this.property = property;
    }

    private void validatePropertyAndPeriod(){
        validateField(property == null, "Property is required.");
        validateField(period == null, "Period is required.");
    }
}
