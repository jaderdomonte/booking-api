package com.hostfully.bookingapi.domain;

import com.hostfully.bookingapi.exceptions.DomainObjectValidationException;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class GuestNameVO {

    private String firstName;

    private String lastName;

    public GuestNameVO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        validate();
    }

    private void validate(){
        if(StringUtils.isEmpty(firstName) || StringUtils.isEmpty(lastName)){
            throw new DomainObjectValidationException("Guest firstName and lastName are required.");
        }
    }
}
