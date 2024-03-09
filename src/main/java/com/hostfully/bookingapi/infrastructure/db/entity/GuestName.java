package com.hostfully.bookingapi.infrastructure.db.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class GuestName {

    private String firstName;

    private String lastName;
}
