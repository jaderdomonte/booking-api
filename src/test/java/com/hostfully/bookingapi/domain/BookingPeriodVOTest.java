package com.hostfully.bookingapi.domain;

import com.hostfully.bookingapi.db.entity.BookingPeriod;
import com.hostfully.bookingapi.exceptions.DomainObjectValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookingPeriodVOTest {

    @Test
    void shouldThrowsExceptionIfCreateDomainObjectWithoutCheckIn() {
        DomainObjectValidationException exception = assertThrows(DomainObjectValidationException.class, () -> {
            new BookingPeriodVO(null, LocalDate.now());
        });

        assertEquals("CheckIn and CheckOut are required.", exception.getMessage());
    }

    @Test
    void shouldThrowsExceptionIfCreateDomainObjectWithoutCheckOut()  {
        DomainObjectValidationException exception = assertThrows(DomainObjectValidationException.class, () -> {
            new BookingPeriodVO(LocalDate.now(), null);
        });

        assertEquals("CheckIn and CheckOut are required.", exception.getMessage());
    }

    @Test
    void shouldThrowsExceptionIfCreateDomainObjectWithoutRequiredFields() {
        DomainObjectValidationException exception = assertThrows(DomainObjectValidationException.class, () -> {
            new BookingPeriodVO(null, null);
        });

        assertEquals("CheckIn and CheckOut are required.", exception.getMessage());
    }

    @Test
    void shouldThrowsExceptionIfCheckInIsEqualCheckOut() {
        DomainObjectValidationException exception = assertThrows(DomainObjectValidationException.class, () -> {
            new BookingPeriodVO(LocalDate.now(), LocalDate.now());
        });

        assertEquals("CheckOut date should be greater than CheckIn date.", exception.getMessage());
    }

    @Test
    void shouldThrowsExceptionIfCheckInIsAfterCheckOut() {
        DomainObjectValidationException exception = assertThrows(DomainObjectValidationException.class, () -> {
            new BookingPeriodVO(LocalDate.now().plusDays(1), LocalDate.now());
        });

        assertEquals("CheckOut date should be greater than CheckIn date.", exception.getMessage());
    }

    @Test
    void shouldCreateDomainObjectWithRequiredFields() {
        BookingPeriod.builder().checkIn(LocalDate.now()).checkOut(LocalDate.now().plusDays(10)).build();

        assertDoesNotThrow(() -> DomainObjectValidationException.class);
    }
}