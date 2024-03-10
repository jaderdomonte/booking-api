package com.hostfully.bookingapi.web.dto;

import java.time.LocalDate;

public record BookingPeriodDto(LocalDate checkIn, LocalDate checkOut){
}
