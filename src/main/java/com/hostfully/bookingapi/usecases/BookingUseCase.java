package com.hostfully.bookingapi.usecases;

import com.hostfully.bookingapi.db.validation.OverlappingValidation;
import com.hostfully.bookingapi.db.entity.BookingEntity;
import com.hostfully.bookingapi.db.entity.GuestEntity;
import com.hostfully.bookingapi.db.enumeration.BookingStatusEnum;
import com.hostfully.bookingapi.db.mapper.BookingEntityDomainMapper;
import com.hostfully.bookingapi.db.repository.BookingRepository;
import com.hostfully.bookingapi.db.repository.GuestRepository;
import com.hostfully.bookingapi.domain.Booking;
import com.hostfully.bookingapi.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@AllArgsConstructor
public class BookingUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(BookingUseCase.class);

    private final BookingRepository bookingRepository;

    private final GuestRepository guestRepository;

    private final OverlappingValidation overlappingValidation;

    private final BookingEntityDomainMapper bookingEntityDomainMapper;

    public Booking getBookingById(Long id){
        LOG.info("Starting get Booking {}", id);
        BookingEntity entity = bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("It does not exists a Booking with id " + id));
//        BookingEntity entity = bookingRepository.findByIdAndStatusId(id, BookingStatusEnum.CONFIRMED.getId()).orElseThrow(() -> new ResourceNotFoundException("It does not exists a Booking with id: " + id));
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
        overlappingValidation.checkOverlappingPeriod(entity.getPeriod().getCheckIn(), entity.getPeriod().getCheckOut());
        bookingRepository.save(entity);
        LOG.info("Booking created");
    }

    public void deleteBooking(Long id){
        LOG.info("Starting delete Booking {}", id);
        BookingEntity bookingEntity = bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("It does not exists a Booking with id " + id));
        bookingRepository.delete(bookingEntity);
        LOG.info("Booking {} deleted", id);
    }

    public void updateBooking(Long id, Booking booking){
        LOG.info("Starting updating Booking {}", id);
        BookingEntity entity = bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("It does not exists a Booking with id " + id));
        GuestEntity guestEntity = guestRepository.findById(booking.getGuest().getId()).orElseThrow(() -> new ResourceNotFoundException("It does not exists a Guest with id " + booking.getGuest().getId()));

        // TODO: validar se a propriedade não está alugada ou bloqueada nesse período. Considerar o status do Booking = 1
        overlappingValidation.checkOverlappingPeriod(booking.getPeriod().getCheckIn(), booking.getPeriod().getCheckOut());

        entity.getPeriod().setCheckIn(booking.getPeriod().getCheckIn());
        entity.getPeriod().setCheckOut(booking.getPeriod().getCheckOut());
        entity.setGuest(guestEntity);

        bookingRepository.save(entity);
        LOG.info("Booking {} updated", id);
    }

    @Transactional
    public void cancelBooking(Long id){
        LOG.info("Starting cancelling Booking {}", id);
        bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("It does not exists a Booking with id " + id));
        bookingRepository.changeBookingStatus(id, BookingStatusEnum.CANCELED.getId());
        LOG.info("Booking {} canceled", id);
    }

    @Transactional
    public void activateBooking(Long id){
        LOG.info("Starting activate Booking {}", id);
        BookingEntity entity = bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("It does not exists a Booking with id " + id));

        // TODO: validar se a propriedade não está alugada ou bloqueada nesse período. Considerar o status do Booking = 1
        overlappingValidation.checkOverlappingPeriod(entity.getPeriod().getCheckIn(), entity.getPeriod().getCheckOut());
        bookingRepository.changeBookingStatus(id, BookingStatusEnum.CONFIRMED.getId());
        LOG.info("Booking {} activated", id);
    }
}
