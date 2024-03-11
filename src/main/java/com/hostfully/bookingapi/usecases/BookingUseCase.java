package com.hostfully.bookingapi.usecases;

import com.hostfully.bookingapi.domain.Booking;
import com.hostfully.bookingapi.exceptions.BookingOverlappingException;
import com.hostfully.bookingapi.exceptions.ResourceNotFoundException;
import com.hostfully.bookingapi.infrastructure.db.entity.BlockingEntity;
import com.hostfully.bookingapi.infrastructure.db.entity.BookingEntity;
import com.hostfully.bookingapi.infrastructure.db.entity.GuestEntity;
import com.hostfully.bookingapi.infrastructure.db.enumeration.BookingStatusEnum;
import com.hostfully.bookingapi.infrastructure.db.mapper.BookingEntityDomainMapper;
import com.hostfully.bookingapi.infrastructure.db.repository.BlockingRepository;
import com.hostfully.bookingapi.infrastructure.db.repository.BookingRepository;
import com.hostfully.bookingapi.infrastructure.db.repository.GuestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class BookingUseCase {

    private final BookingRepository bookingRepository;

    private final GuestRepository guestRepository;

    private final BlockingRepository blockingRepository;

    private final BookingEntityDomainMapper bookingEntityDomainMapper;

    public Booking getBookingById(Long id){
        BookingEntity entity = bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("It does not exists a Booking with id: " + id));
        return bookingEntityDomainMapper.toDomain(entity);
    }

    public List<Booking> getAllBookings(){
        List<BookingEntity> allBookings = bookingRepository.findAll();
        return allBookings.stream().map(booking -> bookingEntityDomainMapper.toDomain(booking)).toList();
    }

    public void createBooking(Booking domain){
        BookingEntity entity = bookingEntityDomainMapper.toEntity(domain);

        // TODO: validar se a propriedade não está alugada ou bloqueada nesse período
        checkOverlappingPeriod(entity.getPeriod().getCheckIn(), entity.getPeriod().getCheckOut());

        bookingRepository.save(entity);
    }

    public void deleteBooking(Long id){
        BookingEntity bookingEntity = bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("It does not exists a Booking with id: " + id));
        bookingRepository.delete(bookingEntity);
    }

    public void updateBooking(Long id, Booking booking){
        BookingEntity entity = bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("It does not exists a Booking with id: " + id));
        GuestEntity guestEntity = guestRepository.findById(booking.getGuest().getId()).orElseThrow(() -> new ResourceNotFoundException("It does not exists a Guest with id: " + booking.getGuest().getId()));

        entity.getPeriod().setCheckIn(booking.getPeriod().getCheckIn());
        entity.getPeriod().setCheckOut(booking.getPeriod().getCheckOut());
        entity.setGuest(guestEntity);

        // TODO: validar se a propriedade não está alugada ou bloqueada nesse período. Considerar o status do Booking = 1
        checkOverlappingPeriod(entity.getPeriod().getCheckIn(), entity.getPeriod().getCheckOut());

        bookingRepository.save(entity);
    }

    @Transactional
    public void cancelBooking(Long id){
        bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("It does not exists a Booking with id: " + id));
        bookingRepository.changeBookingStatus(id, BookingStatusEnum.CANCELED.getId());
    }

    @Transactional
    public void activateBooking(Long id){
        BookingEntity entity = bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("It does not exists a Booking with id: " + id));

        // TODO: validar se a propriedade não está alugada ou bloqueada nesse período. Considerar o status do Booking = 1
        checkOverlappingPeriod(entity.getPeriod().getCheckIn(), entity.getPeriod().getCheckOut());

        bookingRepository.changeBookingStatus(id, BookingStatusEnum.CONFIRMED.getId());
    }

    private void checkOverlappingPeriod(LocalDate checkIn, LocalDate checkOut) {
        int overlappingBookingCount = bookingRepository.checkOverlapping(checkIn, checkOut);
        List<BookingEntity> bookingEntities = bookingRepository.checkOverlappingBookings(checkIn, checkOut);
        System.out.println(bookingEntities.size());

        if (overlappingBookingCount > 0) {
            throw new BookingOverlappingException("This Property is already booked in this period!");
        }

        int overlappingBlockingsCount = blockingRepository.checkOverlapping(checkIn, checkOut);
        List<BlockingEntity> blockingEntities = blockingRepository.checkOverlappingBlockings(checkIn, checkOut);
        System.out.println(blockingEntities.size());

        if (overlappingBlockingsCount > 0) {
            throw new BookingOverlappingException("This Property is already blocked in this period!");
        }
    }
}
