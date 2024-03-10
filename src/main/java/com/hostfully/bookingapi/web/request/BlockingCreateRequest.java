package com.hostfully.bookingapi.web.request;

import java.time.LocalDate;

public record BlockingCreateRequest (Long propertyId, LocalDate checkIn, LocalDate checkOut) {
}
