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
        checkOverlappingBlocking(propertyId, period);
        checkOverlappingBooking(propertyId, period);
    }

    private void checkOverlappingBlocking(Long propertyId, BookingPeriod period) {
        int overlappingBlockingsCount = blockingRepository.checkOverlapping(propertyId, period);

        checkOverlappingCount(overlappingBlockingsCount, "This Property is already blocked in this period!");
    }

    private void checkOverlappingBooking(Long propertyId, BookingPeriod period) {
        int overlappingBookingCount = bookingRepository.checkOverlapping(propertyId, period);

        checkOverlappingCount(overlappingBookingCount, "This Property is already booked in this period!");
    }

    public void checkOverlappingPeriod(Long id, Long propertyId, BookingPeriod period) {
        checkOverlappingBlocking(id, propertyId, period);
        checkOverlappingBooking(id, propertyId, period);
    }

    private void checkOverlappingBlocking(Long id, Long propertyId, BookingPeriod period) {
        int overlappingBlockingsCount = blockingRepository.checkOverlapping(id, propertyId, period);

        checkOverlappingCount(overlappingBlockingsCount, "This Property is already blocked in this period!");
    }

    private void checkOverlappingBooking(Long id, Long propertyId, BookingPeriod period) {
        int overlappingBookingCount = bookingRepository.checkOverlapping(id, propertyId, period);

        checkOverlappingCount(overlappingBookingCount, "This Property is already booked in this period!");
    }

    private void checkOverlappingCount(int overlappingCount, String s) {
        if (overlappingCount > 0) {
            throw new BookingOverlappingException(s);
        }
    }
}
