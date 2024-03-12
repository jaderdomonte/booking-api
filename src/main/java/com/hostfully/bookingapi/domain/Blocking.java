package com.hostfully.bookingapi.domain;

import com.hostfully.bookingapi.db.entity.BookingPeriod;
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
        validate();
    }

    public Blocking(Property property, BookingPeriod period) {
        this.property = property;
        this.period = period;
        validate();
    }

    @Override
    protected void validate(){
        validateField(property == null, "Property is required.");
        validateField(period == null, "Period is required.");
    }
}
