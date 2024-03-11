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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class BookingUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(BookingUseCase.class);

    private final BookingRepository bookingRepository;

    private final GuestRepository guestRepository;

    private final BlockingRepository blockingRepository;

    private final BookingEntityDomainMapper bookingEntityDomainMapper;

    public Booking getBookingById(Long id){
        LOG.info("Starting get Booking");
        BookingEntity entity = bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("It does not exists a Booking with id: " + id));
        LOG.info("Returned Booking {}", id);
        return bookingEntityDomainMapper.toDomain(entity);
    }

    public List<Booking> getAllBookings(){
        LOG.info("Starting get all Bookings");
        List<BookingEntity> allBookings = bookingRepository.findAll();
        LOG.info("Returned {} Bookings", allBookings.size());
        return allBookings.stream().map(booking -> bookingEntityDomainMapper.toDomain(booking)).toList();
    }

    public void createBooking(Booking domain){
        LOG.info("Starting creating Booking");
        BookingEntity entity = bookingEntityDomainMapper.toEntity(domain);

        // TODO: validar se a propriedade não está alugada ou bloqueada nesse período
        checkOverlappingPeriod(entity.getPeriod().getCheckIn(), entity.getPeriod().getCheckOut());
        bookingRepository.save(entity);
        LOG.info("Booking created");
    }

    public void deleteBooking(Long id){
        LOG.info("Starting delete Booking");
        BookingEntity bookingEntity = bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("It does not exists a Booking with id: " + id));
        LOG.info("Booking {} deleted", id);
        bookingRepository.delete(bookingEntity);
    }

    public void updateBooking(Long id, Booking booking){
        LOG.info("Starting updating Booking");
        BookingEntity entity = bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("It does not exists a Booking with id: " + id));
        GuestEntity guestEntity = guestRepository.findById(booking.getGuest().getId()).orElseThrow(() -> new ResourceNotFoundException("It does not exists a Guest with id: " + booking.getGuest().getId()));

        entity.getPeriod().setCheckIn(booking.getPeriod().getCheckIn());
        entity.getPeriod().setCheckOut(booking.getPeriod().getCheckOut());
        entity.setGuest(guestEntity);

        // TODO: validar se a propriedade não está alugada ou bloqueada nesse período. Considerar o status do Booking = 1
        checkOverlappingPeriod(entity.getPeriod().getCheckIn(), entity.getPeriod().getCheckOut());
        LOG.info("Booking {} updated", id);
        bookingRepository.save(entity);
    }

    @Transactional
    public void cancelBooking(Long id){
        LOG.info("Starting cancelling Booking");
        bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("It does not exists a Booking with id: " + id));
        bookingRepository.changeBookingStatus(id, BookingStatusEnum.CANCELED.getId());
        LOG.info("Booking {} canceled", id);
    }

    @Transactional
    public void activateBooking(Long id){
        LOG.info("Starting activate Booking");
        BookingEntity entity = bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("It does not exists a Booking with id: " + id));

        // TODO: validar se a propriedade não está alugada ou bloqueada nesse período. Considerar o status do Booking = 1
        checkOverlappingPeriod(entity.getPeriod().getCheckIn(), entity.getPeriod().getCheckOut());
        LOG.info("Booking {} activated", id);
        bookingRepository.changeBookingStatus(id, BookingStatusEnum.CONFIRMED.getId());
    }

    private void checkOverlappingPeriod(LocalDate checkIn, LocalDate checkOut) {
        checkOverllapingBooking(checkIn, checkOut);
        checkOverllapingBlocking(checkIn, checkOut);
    }

    private void checkOverllapingBlocking(LocalDate checkIn, LocalDate checkOut) {
        int overlappingBlockingsCount = blockingRepository.checkOverlapping(checkIn, checkOut);
        List<BlockingEntity> blockingEntities = blockingRepository.checkOverlappingBlockings(checkIn, checkOut);

        if (overlappingBlockingsCount > 0) {
            throw new BookingOverlappingException("This Property is already blocked in this period!");
        }
    }

    private void checkOverllapingBooking(LocalDate checkIn, LocalDate checkOut) {
        int overlappingBookingCount = bookingRepository.checkOverlapping(checkIn, checkOut);
        List<BookingEntity> bookingEntities = bookingRepository.checkOverlappingBookings(checkIn, checkOut);

        if (overlappingBookingCount > 0) {
            throw new BookingOverlappingException("This Property is already booked in this period!");
        }
    }
}
