package com.hostfully.bookingapi.db.validation;

import com.hostfully.bookingapi.db.repository.BlockingRepository;
import com.hostfully.bookingapi.db.repository.BookingRepository;
import com.hostfully.bookingapi.exceptions.BookingOverlappingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OverlappingValidationTest {

    @InjectMocks
    private OverlappingValidation validation;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BlockingRepository blockingRepository;

    private LocalDate checkIn;

    private LocalDate checkOut;

    @BeforeEach
    void setUp(){
        checkIn = LocalDate.now();
        checkOut = LocalDate.now().plusDays(10);
    }

    @Test
    void shouldCheckOverlappingPeriodOnSucess(){
        when(bookingRepository.checkOverlapping(any(), any())).thenReturn(0);
        when(blockingRepository.checkOverlapping(any(), any())).thenReturn(0);

        validation.checkOverlappingPeriod(checkIn, checkOut);

        assertDoesNotThrow(() -> BookingOverlappingException.class);
        verify(blockingRepository).checkOverlapping(any(), any());
        verify(bookingRepository).checkOverlapping(any(), any());
    }

    @Test
    void shouldThrowExceptionWhenThereIsBlockingOverlapping(){
        when(blockingRepository.checkOverlapping(any(), any())).thenReturn(1);

        Assertions.assertThrows(BookingOverlappingException.class, () ->{
            validation.checkOverlappingPeriod(checkIn, checkOut);
        });

        verify(blockingRepository).checkOverlapping(any(), any());
    }

    @Test
    void shouldThrowExceptionWhenThereIsBookingOverlapping(){
        when(bookingRepository.checkOverlapping(any(), any())).thenReturn(1);

        Assertions.assertThrows(BookingOverlappingException.class, () ->{
            validation.checkOverlappingPeriod(checkIn, checkOut);
        });

        verify(bookingRepository).checkOverlapping(any(), any());
    }
}