package com.hostfully.bookingapi.domain;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class GuestNameVO extends ValidatedDomain {

    private String firstName;

    private String lastName;

    public GuestNameVO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        validateFullName();
    }

    private void validateFullName(){
        validateField(StringUtils.isEmpty(firstName) || StringUtils.isEmpty(lastName), "Guest firstName and lastName are required.");
    }
}
