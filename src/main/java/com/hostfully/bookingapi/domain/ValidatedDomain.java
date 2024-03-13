package com.hostfully.bookingapi.domain;

import com.hostfully.bookingapi.exceptions.DomainObjectValidationException;

public interface ValidatedDomain {

    void validate();


    default void validateField(boolean condition, String s) {
        if (condition) {
            throw new DomainObjectValidationException(s);
        }
    }
}
