package com.hostfully.bookingapi.domain;

import com.hostfully.bookingapi.exceptions.DomainObjectValidationException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class Property {

    private Long id;

    private String name;

    public Property(String name){
        this.name = name;
    }

    public Property(Long id, String name) {
        this(name);
        this.id = id;
        validate();
    }

    private void validate(){
        if(StringUtils.isEmpty(name)){
            throw new DomainObjectValidationException("Property name is required.");
        }
    }
}
