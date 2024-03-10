package com.hostfully.bookingapi.domain;

import com.hostfully.bookingapi.exceptions.DomainObjectValidationException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Guest {

    private Long id;

    private GuestNameVO fullName;

    public Guest(GuestNameVO fullName){
        this.fullName = fullName;
        validate();
    }

    public Guest(Long id, GuestNameVO fullName) {
        this(fullName);
        this.id = id;
    }

    private void validate(){
        if(fullName == null){
            throw new DomainObjectValidationException("Guest fullName is required.");
        }
    }
}
