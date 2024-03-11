package com.hostfully.bookingapi.web.response;

import com.hostfully.bookingapi.web.dto.BookingPeriodDto;
import com.hostfully.bookingapi.web.dto.GuestDto;
import com.hostfully.bookingapi.web.dto.PropertyDto;

public record BookingResponse(Long id, GuestDto guest, PropertyDto property, BookingPeriodDto period, String status) {
}
