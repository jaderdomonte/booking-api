package com.hostfully.bookingapi.domain;

import com.hostfully.bookingapi.exceptions.DomainObjectValidationException;

public class ValidatedDomain {

    protected void validateField(boolean condition, String s) {
        if (condition) {
            throw new DomainObjectValidationException(s);
        }
    }
}
