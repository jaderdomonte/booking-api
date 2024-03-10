package com.hostfully.bookingapi.infrastructure.db.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class BookingPeriod {

    private LocalDate checkIn;

    private LocalDate checkOut;

}
