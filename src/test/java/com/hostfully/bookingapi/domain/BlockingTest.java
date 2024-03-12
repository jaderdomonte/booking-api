package com.hostfully.bookingapi.domain;

import com.hostfully.bookingapi.db.entity.BookingPeriod;
import com.hostfully.bookingapi.exceptions.DomainObjectValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BlockingTest {

    @Test
    void shouldThrowsExceptionIfCreateDomainObjectWithoutProperty() {
        DomainObjectValidationException exception = assertThrows(DomainObjectValidationException.class, () -> {
            BookingPeriod bookingPeriod = BookingPeriod.builder().checkIn(LocalDate.now()).checkOut(LocalDate.now().plusDays(10)).build();
            new Blocking(null, bookingPeriod);
        });

        assertEquals("Property is required.", exception.getMessage());
    }

    @Test
    void shouldThrowsExceptionIfCreateDomainObjectWithoutPeriod() {
        DomainObjectValidationException exception = assertThrows(DomainObjectValidationException.class, () -> {
            Property property = new Property(1L);
            new Blocking(property, null);
        });

        assertEquals("Period is required.", exception.getMessage());
    }

    @Test
    void shouldThrowsExceptionIfCreateDomainObjectWithoutRequiredFields() {
        assertThrows(DomainObjectValidationException.class, () -> {
            new Blocking(null, null);
        });
    }

    @Test
    void shouldCreateDomainObjectWithRequiredFields() {
        Property property = new Property(1L);
        BookingPeriod bookingPeriod = BookingPeriod.builder().checkIn(LocalDate.now()).checkOut(LocalDate.now().plusDays(10)).build();
        new Blocking(property, bookingPeriod);

        assertDoesNotThrow(() -> DomainObjectValidationException.class);
    }
}