package com.hostfully.bookingapi.domain;

import com.hostfully.bookingapi.exceptions.DomainObjectValidationException;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class BookingStatusVO {

    private Long id;

    private String description;

    public BookingStatusVO(String description){
        this.description = description;
        validate();
    }

    public BookingStatusVO(Long id, String description) {
        this(description);
        this.description = description;
    }

    private void validate(){
        if(StringUtils.isEmpty(description)){
            throw new DomainObjectValidationException("Status description is required.");
        }
    }
}
