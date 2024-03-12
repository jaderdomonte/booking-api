package com.hostfully.bookingapi.usecases;

import com.hostfully.bookingapi.db.validation.OverlappingValidation;
import com.hostfully.bookingapi.db.entity.BlockingEntity;
import com.hostfully.bookingapi.db.mapper.BlockingEntityDomainMapper;
import com.hostfully.bookingapi.db.repository.BlockingRepository;
import com.hostfully.bookingapi.domain.Blocking;
import com.hostfully.bookingapi.exceptions.ResourceNotFoundException;
import com.hostfully.bookingapi.web.dto.BookingPeriodDto;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class BlockingUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(BlockingUseCase.class);

    private final BlockingRepository blockingRepository;

    private final OverlappingValidation overlappingValidation;

    private final BlockingEntityDomainMapper blockingEntityDomainMapper;

    public List<Blocking> getAllBlockings(){
        LOG.info("Starting get all Blockings");
        List<BlockingEntity> allBlockings = blockingRepository.findAll();
        LOG.info("Returned {} Blockings", allBlockings.size());
        return allBlockings.stream().map(blocking -> blockingEntityDomainMapper.toDomain(blocking)).toList();
    }

    public void createBlocking(Blocking blocking){
        LOG.info("Starting creating Blocking");
        BlockingEntity entity = blockingEntityDomainMapper.toEntity(blocking);

        // TODO: validar se a propriedade não está alugada ou bloqueada nesse período. Considerar o status do Booking = 1
        overlappingValidation.checkOverlappingPeriod(entity.getPeriod().getCheckIn(), entity.getPeriod().getCheckOut());

        blockingRepository.save(entity);
        LOG.info("Blocking created");
    }

    public void deleteBlocking(Long id){
        LOG.info("Starting delete Blocking {}", id);
        BlockingEntity blockingEntity = blockingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("It does not exists a Block with id: " + id));
        blockingRepository.delete(blockingEntity);
        LOG.info("Blocking {} deleted", id);
    }

    public void updateBlocking(Long id, BookingPeriodDto bookingPeriod){
        LOG.info("Starting updating Blocking {}", id);
        BlockingEntity entity = blockingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("It does not exists a Block with id: " + id));

        // TODO: validar se a propriedade não está alugada ou bloqueada nesse período. Considerar o status do Booking = 1
        overlappingValidation.checkOverlappingPeriod(bookingPeriod.checkIn(), bookingPeriod.checkOut());

        entity.getPeriod().setCheckIn(bookingPeriod.checkIn());
        entity.getPeriod().setCheckOut(bookingPeriod.checkOut());

        blockingRepository.save(entity);
        LOG.info("Blocking {} updated", id);
    }
}
