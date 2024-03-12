package com.hostfully.bookingapi.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Guest extends ValidatedDomain {

    private Long id;

    private GuestNameVO fullName;

    public Guest(Long id) {
        this.id = id;
    }

    public Guest(Long id, GuestNameVO fullName) {
        this.id = id;
        this.fullName = fullName;
        validate();
    }

    protected void validate(){
        validateField(id == null && fullName == null, "Guest fullName is required.");
    }
}
