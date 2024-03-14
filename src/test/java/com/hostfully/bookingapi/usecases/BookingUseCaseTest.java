package com.hostfully.bookingapi.usecases;

import com.hostfully.bookingapi.db.entity.*;
import com.hostfully.bookingapi.db.enumeration.BookingStatusEnum;
import com.hostfully.bookingapi.db.mapper.BookingEntityDomainMapper;
import com.hostfully.bookingapi.db.repository.BookingRepository;
import com.hostfully.bookingapi.db.repository.GuestRepository;
import com.hostfully.bookingapi.db.repository.PropertyRepository;
import com.hostfully.bookingapi.db.validation.OverlappingValidation;
import com.hostfully.bookingapi.domain.*;
import com.hostfully.bookingapi.exceptions.BookingOverlappingException;
import com.hostfully.bookingapi.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingUseCaseTest {

    @InjectMocks
    private BookingUseCase useCase;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private OverlappingValidation overlappingValidation;

    @Mock
    private BookingEntityDomainMapper mapper;

    private Booking domain;

    private BookingEntity entity;

    @BeforeEach
    void setUp(){
        GuestNameVO guestNameVO = new GuestNameVO("Jordan", "Love");
        Guest guest = new Guest(1L, guestNameVO);
        Property property = new Property(1L, "Beach House");
        BookingPeriodVO bookingPeriod = new BookingPeriodVO(LocalDate.now(), LocalDate.now().plusDays(10));
        BookingStatusVO bookingStatusVO = new BookingStatusVO(BookingStatusEnum.CANCELED.getId(), BookingStatusEnum.CANCELED.getDescription());

        domain = new Booking(1L, guest, property, bookingPeriod, bookingStatusVO);

        GuestEntity guestEntity = GuestEntity.builder().id(domain.getGuest().getId()).build();
        BookingPeriod bookingPeriodEntity = BookingPeriod.builder().checkIn(domain.getPeriod().getCheckIn()).checkOut(domain.getPeriod().getCheckOut()).build();
        BookingStatusEntity bookingStatusEntity = BookingStatusEntity.builder().id(BookingStatusEnum.CONFIRMED.getId()).build();
        PropertyEntity propertyEntity = PropertyEntity.builder().id(domain.getProperty().getId()).name(domain.getProperty().getName()).build();

        entity = BookingEntity.builder().guest(guestEntity).property(propertyEntity).period(bookingPeriodEntity).status(bookingStatusEntity).build();
    }

    @Test
    void shouldReturnAllBookings(){
        when(bookingRepository.findAll()).thenReturn(asList(BookingEntity.builder().build()));
        when(mapper.toDomain(any())).thenReturn(domain);

        List<Booking> allBookings = useCase.getAllBookings();

        assertEquals(1, allBookings.size());
    }

    @Test
    void shouldReturnBookingById(){
        when(bookingRepository.findById(any())).thenReturn(Optional.of(BookingEntity.builder().build()));
        when(mapper.toDomain(any())).thenReturn(domain);

        Booking booking = useCase.getBookingById(1L);

        assertNotNull(booking);
    }

    @Test
    void shouldThrowsExceptionWhenResourceDoesNotExistsOnGetBookingById(){
        when(bookingRepository.findById(any())).thenThrow(new ResourceNotFoundException("Message"));

        assertThrows(ResourceNotFoundException.class, () ->{
            useCase.getBookingById(1L);
        });

        verify(mapper, never()).toDomain(any());
    }



    @Test
    void shouldCreateBooking(){
        when(mapper.toEntity(any())).thenReturn(entity);
        doNothing().when(overlappingValidation).checkOverlappingPeriod(any(), any());
        when(bookingRepository.save(any())).thenReturn(entity);
        when(propertyRepository.findById(any())).thenReturn(Optional.of(PropertyEntity.builder().build()));
        when(guestRepository.findById(any())).thenReturn(Optional.of(GuestEntity.builder().id(2L).build()));

        useCase.createBooking(domain);

        verify(overlappingValidation).checkOverlappingPeriod(any(), any());
        verify(bookingRepository).save(any());
    }

    @Test
    void shouldThrowExceptionWhenThereIsOverlappingPeriodOnCreate() {
        when(mapper.toEntity(any())).thenReturn(entity);
        when(propertyRepository.findById(any())).thenReturn(Optional.of(PropertyEntity.builder().build()));
        when(guestRepository.findById(any())).thenReturn(Optional.of(GuestEntity.builder().id(2L).build()));
        doThrow(new BookingOverlappingException()).when(overlappingValidation).checkOverlappingPeriod(any(), any());

        assertThrows(BookingOverlappingException.class, () -> {
            useCase.createBooking(domain);
        });

        verify(overlappingValidation, only()).checkOverlappingPeriod(any(), any());
        verify(propertyRepository, only()).findById(any());
        verify(guestRepository, never()).save(any());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenPropertyDoesNotExistsOnCreate() {
        when(mapper.toEntity(any())).thenReturn(entity);
        when(propertyRepository.findById(any())).thenThrow(new ResourceNotFoundException("Message"));
        when(guestRepository.findById(any())).thenReturn(Optional.of(GuestEntity.builder().id(2L).build()));

        assertThrows(ResourceNotFoundException.class, () -> {
            useCase.createBooking(domain);
        });

        verify(overlappingValidation, never()).checkOverlappingPeriod(any(), any());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void shouldDeleteBooking(){
        when(bookingRepository.findById(any())).thenReturn(Optional.of(entity));
        doNothing().when(bookingRepository).delete(any());

        useCase.deleteBooking(1L);

        verify(bookingRepository).delete(any());
    }

    @Test
    void shouldThrowsExceptionWhenResourceDoesNotExistsOnDelete(){
        when(bookingRepository.findById(any())).thenThrow(new ResourceNotFoundException("Message"));

        assertThrows(ResourceNotFoundException.class, () ->{
            useCase.deleteBooking(1L);
        });

        verify(bookingRepository, never()).delete(any());
    }

    @Test
    void shouldUpdateBooking(){
        when(bookingRepository.save(any())).thenReturn(entity);
        doNothing().when(overlappingValidation).checkOverlappingPeriod(any(), any(), any());
        when(bookingRepository.findById(any())).thenReturn(Optional.of(entity));
        when(guestRepository.findById(any())).thenReturn(Optional.of(GuestEntity.builder().id(2L).build()));

        useCase.updateBooking(1L, domain);

        assertTrue(entity.getPeriod().getCheckIn().isEqual(domain.getPeriod().getCheckIn()));
        assertTrue(entity.getPeriod().getCheckOut().isEqual(domain.getPeriod().getCheckOut()));
        assertTrue(entity.getGuest().getId().equals(2L));

        verify(overlappingValidation).checkOverlappingPeriod(any(), any(), any());
        verify(bookingRepository).save(any());
        verify(bookingRepository).findById(any());
        verify(guestRepository).findById(any());
    }

    @Test
    void shouldThrowsExceptionWhenResourceDoesNotExistsOnUpdate(){
        when(bookingRepository.findById(any())).thenThrow(new ResourceNotFoundException("Message"));

        assertThrows(ResourceNotFoundException.class, () ->{
            useCase.updateBooking(1L, domain);
        });

        verify(overlappingValidation, never()).checkOverlappingPeriod(any(), any(), any());
        verify(bookingRepository, never()).save(any());
        verify(guestRepository, never()).findById(any());
    }

    @Test
    void shouldThrowsExceptionWhenGuestDoesNotExistsOnUpdate(){
        when(bookingRepository.findById(any())).thenReturn(Optional.of(entity));
        when(guestRepository.findById(any())).thenThrow(new ResourceNotFoundException("Message"));

        assertThrows(ResourceNotFoundException.class, () ->{
            useCase.updateBooking(1L, domain);
        });

        verify(overlappingValidation, never()).checkOverlappingPeriod(any(), any(), any());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenThereIsOverlappingPeriodOnUpdate() {
        when(bookingRepository.findById(any())).thenReturn(Optional.of(entity));
        when(guestRepository.findById(any())).thenReturn(Optional.of(GuestEntity.builder().id(2L).build()));
        doThrow(new BookingOverlappingException()).when(overlappingValidation).checkOverlappingPeriod(any(), any(), any());

        assertThrows(BookingOverlappingException.class, () -> {
            useCase.updateBooking(1L, domain);
        });

        verify(bookingRepository).findById(any());
        verify(guestRepository).findById(any());
        verify(overlappingValidation).checkOverlappingPeriod(any(), any(), any());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void shouldCancelBooking(){
        when(bookingRepository.findById(any())).thenReturn(Optional.of(entity));
        doNothing().when(bookingRepository).changeBookingStatus(any(), any());

        useCase.cancelBooking(1L);

        verify(bookingRepository).findById(any());
        verify(bookingRepository).changeBookingStatus(any(), any());
    }

    @Test
    void shouldThrowsExceptionWhenResourceDoesNotExistsOnCancel(){
        when(bookingRepository.findById(any())).thenThrow(new ResourceNotFoundException("Message"));

        assertThrows(ResourceNotFoundException.class, () ->{
            useCase.cancelBooking(1L);
        });

        verify(bookingRepository).findById(any());
        verify(bookingRepository, never()).changeBookingStatus(any(), any());
    }

    @Test
    void shouldActivateBooking(){
        when(bookingRepository.findById(any())).thenReturn(Optional.of(entity));
        doNothing().when(overlappingValidation).checkOverlappingPeriod(any(), any());
        doNothing().when(bookingRepository).changeBookingStatus(any(), any());

        useCase.activateBooking(1L);

        verify(bookingRepository).findById(any());
        verify(overlappingValidation).checkOverlappingPeriod(any(), any());
        verify(bookingRepository).changeBookingStatus(any(), any());
    }

    @Test
    void shouldThrowsExceptionWhenResourceDoesNotExistsOnActivate(){
        when(bookingRepository.findById(any())).thenThrow(new ResourceNotFoundException("Message"));

        assertThrows(ResourceNotFoundException.class, () ->{
            useCase.activateBooking(1L);
        });

        verify(bookingRepository).findById(any());
        verify(overlappingValidation, never()).checkOverlappingPeriod(any(), any());
        verify(bookingRepository, never()).changeBookingStatus(any(), any());
    }

    @Test
    void shouldThrowExceptionWhenThereIsOverlappingPeriodOnActivate() {
        when(bookingRepository.findById(any())).thenReturn(Optional.of(entity));
        doThrow(new BookingOverlappingException()).when(overlappingValidation).checkOverlappingPeriod(any(), any());

        assertThrows(BookingOverlappingException.class, () -> {
            useCase.activateBooking(1L);
        });

        verify(bookingRepository).findById(any());
        verify(overlappingValidation).checkOverlappingPeriod(any(), any());
        verify(bookingRepository, never()).changeBookingStatus(any(), any());
    }
}