package com.hostfully.bookingapi.usecases;

import com.hostfully.bookingapi.db.entity.BlockingEntity;
import com.hostfully.bookingapi.db.entity.BookingPeriod;
import com.hostfully.bookingapi.db.entity.PropertyEntity;
import com.hostfully.bookingapi.db.mapper.BlockingEntityDomainMapper;
import com.hostfully.bookingapi.db.repository.BlockingRepository;
import com.hostfully.bookingapi.db.validation.OverlappingValidation;
import com.hostfully.bookingapi.domain.Blocking;
import com.hostfully.bookingapi.domain.Property;
import com.hostfully.bookingapi.exceptions.BookingOverlappingException;
import com.hostfully.bookingapi.exceptions.ResourceNotFoundException;
import com.hostfully.bookingapi.web.dto.BookingPeriodDto;
import org.junit.jupiter.api.Assertions;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlockingUseCaseTest {

    @InjectMocks
    private BlockingUseCase useCase;

    @Mock
    private BlockingRepository blockingRepository;

    @Mock
    private OverlappingValidation overlappingValidation;

    @Mock
    private BlockingEntityDomainMapper mapper;

    private Blocking domain;

    private BlockingEntity entity;

    @BeforeEach
    void setUp(){
        Property property = new Property(1L, "Beach House");
        BookingPeriod bookingPeriod = BookingPeriod.builder().checkIn(LocalDate.now()).checkOut(LocalDate.now().plusDays(10)).build();
        domain = new Blocking(property, bookingPeriod);

        BookingPeriod bookingPeriodEntity = BookingPeriod.builder().checkIn(domain.getPeriod().getCheckIn()).checkOut(domain.getPeriod().getCheckOut()).build();
        PropertyEntity propertyEntity = PropertyEntity.builder().id(domain.getProperty().getId()).name(domain.getProperty().getName()).build();
        entity = BlockingEntity.builder().property(propertyEntity).period(bookingPeriodEntity).build();
    }

    @Test
    void shouldReturnAllBlockings(){
        when(blockingRepository.findAll()).thenReturn(asList(BlockingEntity.builder().build()));
        when(mapper.toDomain(any())).thenReturn(domain);

        List<Blocking> allBlockings = useCase.getAllBlockings();

        Assertions.assertEquals(1, allBlockings.size());
    }

    @Test
    void shouldCreateBlocking(){
        when(mapper.toEntity(any())).thenReturn(entity);
        doNothing().when(overlappingValidation).checkOverlappingPeriod(any(), any());
        when(blockingRepository.save(any())).thenReturn(BlockingEntity.builder().build());

        useCase.createBlocking(domain);

        verify(overlappingValidation).checkOverlappingPeriod(any(), any());
        verify(blockingRepository).save(any());
    }

    @Test
    void shouldThrowExceptionWhenThereIsOverlappingPeriodOnCreate() {
        when(mapper.toEntity(any())).thenReturn(entity);
        doThrow(new BookingOverlappingException()).when(overlappingValidation).checkOverlappingPeriod(any(), any());

        assertThrows(BookingOverlappingException.class, () -> {
            useCase.createBlocking(domain);
        });

        verify(overlappingValidation, only()).checkOverlappingPeriod(any(), any());
        verify(blockingRepository, never()).save(any());
    }

    @Test
    void shouldDeleteBlocking(){
        when(blockingRepository.findById(any())).thenReturn(Optional.of(BlockingEntity.builder().build()));
        doNothing().when(blockingRepository).delete(any());

        useCase.deleteBlocking(1L);

        verify(blockingRepository).delete(any());
    }

    @Test
    void shouldThrowsExceptionWhenResourceDoesNotExistsOnDelete(){
        when(blockingRepository.findById(any())).thenThrow(new ResourceNotFoundException("Message"));

        assertThrows(ResourceNotFoundException.class, () ->{
            useCase.deleteBlocking(1L);
        });

        verify(blockingRepository, never()).delete(any());
    }

    @Test
    void shouldUpdateBlocking(){
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate nextWeek = LocalDate.now().plusDays(7);
        LocalDate nextWeekPlusOne = LocalDate.now().plusDays(8);

        BookingPeriodDto periodDto = new BookingPeriodDto(today, tomorrow);
        BookingPeriod period = BookingPeriod.builder().checkIn(nextWeek).checkOut(nextWeekPlusOne).build();
        BlockingEntity entity = BlockingEntity.builder().period(period).build();

        when(blockingRepository.save(any())).thenReturn(entity);
        doNothing().when(overlappingValidation).checkOverlappingPeriod(any(), any());
        when(blockingRepository.findById(any())).thenReturn(Optional.of(entity));

        useCase.updateBlocking(1L, periodDto);

        assertTrue(entity.getPeriod().getCheckIn().isEqual(periodDto.checkIn()));
        assertTrue(entity.getPeriod().getCheckOut().isEqual(periodDto.checkOut()));

        verify(overlappingValidation).checkOverlappingPeriod(any(), any());
        verify(blockingRepository).save(any());
        verify(blockingRepository).findById(any());
    }

    @Test
    void shouldThrowExceptionWhenThereIsOverlappingPeriodOnUpdate() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate nextWeek = LocalDate.now().plusDays(7);
        LocalDate nextWeekPlusOne = LocalDate.now().plusDays(8);

        BookingPeriodDto periodDto = new BookingPeriodDto(today, tomorrow);
        BookingPeriod period = BookingPeriod.builder().checkIn(nextWeek).checkOut(nextWeekPlusOne).build();
        BlockingEntity entity = BlockingEntity.builder().period(period).build();

        when(blockingRepository.findById(any())).thenReturn(Optional.of(entity));
        doThrow(new BookingOverlappingException()).when(overlappingValidation).checkOverlappingPeriod(any(), any());

        assertThrows(BookingOverlappingException.class, () -> {
            useCase.updateBlocking(1L, periodDto);
        });

        verify(overlappingValidation).checkOverlappingPeriod(any(), any());
        verify(blockingRepository).findById(any());
        verify(blockingRepository, never()).save(any());
    }

    @Test
    void shouldThrowsExceptionWhenResourceDoesNotExistsOnUpdate(){
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        BookingPeriodDto periodDto = new BookingPeriodDto(today, tomorrow);

        when(blockingRepository.findById(any())).thenThrow(new ResourceNotFoundException("Message"));

        assertThrows(ResourceNotFoundException.class, () ->{
            useCase.updateBlocking(1L, periodDto);
        });

        verify(overlappingValidation, never()).checkOverlappingPeriod(any(), any());
        verify(blockingRepository, never()).save(any());
    }
}