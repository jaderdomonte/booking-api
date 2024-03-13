package com.hostfully.bookingapi.db.validation;

import com.hostfully.bookingapi.db.entity.BookingPeriod;
import com.hostfully.bookingapi.db.repository.BlockingRepository;
import com.hostfully.bookingapi.db.repository.BookingRepository;
import com.hostfully.bookingapi.exceptions.BookingOverlappingException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OverlappingValidation {

    private static final Logger LOG = LoggerFactory.getLogger(OverlappingValidation.class);

    private final BookingRepository bookingRepository;

    private final BlockingRepository blockingRepository;

    public void checkOverlappingPeriod(Long propertyId, BookingPeriod period) {
        checkOverllapingBlocking(propertyId, period);
        checkOverllapingBooking(propertyId, period);
    }

    private void checkOverllapingBlocking(Long propertyId, BookingPeriod period) {
        int overlappingBlockingsCount = blockingRepository.checkOverlapping(propertyId, period);
//        List<BlockingEntity> blockingEntities = blockingRepository.checkOverlappingBlockings(checkIn, checkOut);

        if (overlappingBlockingsCount > 0) {
            throw new BookingOverlappingException("This Property is already blocked in this period!");
        }
    }

    private void checkOverllapingBooking(Long propertyId, BookingPeriod period) {
        int overlappingBookingCount = bookingRepository.checkOverlapping(propertyId, period);
//        List<BookingEntity> bookingEntities = bookingRepository.checkOverlappingBookings(checkIn, checkOut);

        if (overlappingBookingCount > 0) {
            throw new BookingOverlappingException("This Property is already booked in this period!");
        }
    }
}
