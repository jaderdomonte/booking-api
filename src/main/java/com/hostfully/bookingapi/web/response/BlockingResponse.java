package com.hostfully.bookingapi.web.response;

import com.hostfully.bookingapi.web.dto.BookingPeriodDto;
import com.hostfully.bookingapi.web.dto.PropertyDto;

public record BlockingResponse(Long id, PropertyDto property, BookingPeriodDto period) {
}
