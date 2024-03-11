package com.hostfully.bookingapi.domain;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class BookingStatusVO extends ValidatedDomain {

    private Long id;

    private String description;

    public BookingStatusVO(String description){
        this.description = description;
        validateDescription();
    }

    public BookingStatusVO(Long id, String description) {
        this(description);
        this.description = description;
    }

    private void validateDescription(){
        validateField(StringUtils.isEmpty(description), "Status description is required.");
    }
}
