package com.hostfully.bookingapi.usecases;

import com.hostfully.bookingapi.domain.Blocking;
import com.hostfully.bookingapi.exceptions.BookingOverlappingException;
import com.hostfully.bookingapi.exceptions.ResourceNotFoundException;
import com.hostfully.bookingapi.infrastructure.db.entity.BlockingEntity;
import com.hostfully.bookingapi.infrastructure.db.entity.BookingEntity;
import com.hostfully.bookingapi.infrastructure.db.mapper.BlockingEntityDomainMapper;
import com.hostfully.bookingapi.infrastructure.db.repository.BlockingRepository;
import com.hostfully.bookingapi.infrastructure.db.repository.BookingRepository;
import com.hostfully.bookingapi.web.dto.BookingPeriodDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class BlockingUseCase {

    private final BlockingRepository blockingRepository;

    private final BookingRepository bookingRepository;

    private final BlockingEntityDomainMapper blockingEntityDomainMapper;

    public List<Blocking> getAllBlockings(){
        List<BlockingEntity> allBlockings = blockingRepository.findAll();
        return allBlockings.stream().map(blocking -> blockingEntityDomainMapper.toDomain(blocking)).toList();
    }

    public void createBlocking(Blocking blocking){
        BlockingEntity entity = blockingEntityDomainMapper.toEntity(blocking);

        // TODO: validar se a propriedade não está alugada ou bloqueada nesse período. Considerar o status do Booking = 1

        checkOverlappingPeriod(entity.getPeriod().getCheckIn(), entity.getPeriod().getCheckOut());

        blockingRepository.save(entity);
    }

    public void deleteBlocking(Long id){
        BlockingEntity blockingEntity = blockingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("It does not exists a Block with id: " + id));
        blockingRepository.delete(blockingEntity);
    }

    public void updateBlocking(Long id, BookingPeriodDto bookingPeriod){
        BlockingEntity entity = blockingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("It does not exists a Block with id: " + id));

        // TODO: validar se a propriedade não está alugada ou bloqueada nesse período. Considerar o status do Booking = 1
        checkOverlappingPeriod(bookingPeriod.checkIn(), bookingPeriod.checkOut());

        entity.getPeriod().setCheckIn(bookingPeriod.checkIn());
        entity.getPeriod().setCheckOut(bookingPeriod.checkOut());

        blockingRepository.save(entity);
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
