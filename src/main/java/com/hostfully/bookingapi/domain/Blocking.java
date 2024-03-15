package com.hostfully.bookingapi.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Blocking implements ValidatedDomain {

    private Long id;

    private Property property;

    private PeriodVO period;

    public Blocking(Long id, Property property, PeriodVO period) {
        this(property, period);
        this.id = id;
        validate();
    }

    public Blocking(Property property, PeriodVO period) {
        this.property = property;
        this.period = period;
        validate();
    }

    public void validate(){
        validateField(property == null, "Property is required.");
        validateField(period == null, "Period is required.");
    }
}
