package com.hostfully.bookingapi.db.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingPeriod {

    private LocalDate checkIn;

    private LocalDate checkOut;
}
