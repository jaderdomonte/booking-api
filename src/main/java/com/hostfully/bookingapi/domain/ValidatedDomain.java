package com.hostfully.bookingapi.domain;

import com.hostfully.bookingapi.exceptions.DomainObjectValidationException;

public abstract class ValidatedDomain {

    protected abstract void validate();

    protected void validateField(boolean condition, String s) {
        if (condition) {
            throw new DomainObjectValidationException(s);
        }
    }
}
