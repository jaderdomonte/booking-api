package com.hostfully.bookingapi.db.validation;

import com.hostfully.bookingapi.db.repository.BlockingRepository;
import com.hostfully.bookingapi.db.repository.BookingRepository;
import com.hostfully.bookingapi.exceptions.BookingOverlappingException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class OverlappingValidation {

    private static final Logger LOG = LoggerFactory.getLogger(OverlappingValidation.class);

    private final BookingRepository bookingRepository;

    private final BlockingRepository blockingRepository;

    public void checkOverlappingPeriod(LocalDate checkIn, LocalDate checkOut) {
        checkOverllapingBlocking(checkIn, checkOut);
        checkOverllapingBooking(checkIn, checkOut);
    }

    private void checkOverllapingBlocking(LocalDate checkIn, LocalDate checkOut) {
        int overlappingBlockingsCount = blockingRepository.checkOverlapping(checkIn, checkOut);
//        List<BlockingEntity> blockingEntities = blockingRepository.checkOverlappingBlockings(checkIn, checkOut);

        if (overlappingBlockingsCount > 0) {
            throw new BookingOverlappingException("This Property is already blocked in this period!");
        }
    }

    private void checkOverllapingBooking(LocalDate checkIn, LocalDate checkOut) {
        int overlappingBookingCount = bookingRepository.checkOverlapping(checkIn, checkOut);
//        List<BookingEntity> bookingEntities = bookingRepository.checkOverlappingBookings(checkIn, checkOut);

        if (overlappingBookingCount > 0) {
            throw new BookingOverlappingException("This Property is already booked in this period!");
        }
    }
}
