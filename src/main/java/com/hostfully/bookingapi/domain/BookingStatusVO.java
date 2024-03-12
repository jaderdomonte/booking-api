package com.hostfully.bookingapi.domain;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class BookingStatusVO extends ValidatedDomain {

    private Long id;

    private String description;

    public BookingStatusVO(String description){
        this.description = description;
        validate();
    }

    public BookingStatusVO(Long id, String description) {
        this(description);
        this.id = id;
    }

    protected void validate(){
        validateField(StringUtils.isEmpty(description), "Description is required.");
    }
}
