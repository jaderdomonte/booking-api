package com.hostfully.bookingapi.domain;

import com.hostfully.bookingapi.exceptions.DomainObjectValidationException;
import com.hostfully.bookingapi.infrastructure.db.entity.BookingPeriod;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Blocking {

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

    private void validate(){
        if(property == null){
            throw new DomainObjectValidationException("Property is required.");
        }

        if(period == null){
            throw new DomainObjectValidationException("Period is required.");
        }
    }
}
