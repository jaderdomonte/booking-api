package com.hostfully.bookingapi.usecases;

import com.hostfully.bookingapi.domain.Blocking;
import com.hostfully.bookingapi.exceptions.ResourceNotFoundException;
import com.hostfully.bookingapi.infrastructure.db.entity.BlockingEntity;
import com.hostfully.bookingapi.infrastructure.db.mapper.BlockingEntityMapper;
import com.hostfully.bookingapi.infrastructure.db.repository.BlockingRepository;
import com.hostfully.bookingapi.infrastructure.db.repository.BookingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class BlockingUseCase {

    private final BlockingRepository blockingRepository;

    private final BookingRepository bookingRepository;

    private final BlockingEntityMapper blockingEntityMapper;

    public List<Blocking> getAllBlockings(){
        List<BlockingEntity> blockingsEntity = blockingRepository.findAll();
        return blockingsEntity.stream().map(blocking -> blockingEntityMapper.toDomain(blocking)).toList();
    }

    public void createBlocking(Blocking blocking){
        BlockingEntity entity = blockingEntityMapper.toEntity(blocking);
        blockingRepository.save(entity);
    }

    public void deleteBlocking(Long id){
        BlockingEntity blockingEntity = blockingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("It does not exists a Block with id: " + id));
        blockingRepository.delete(blockingEntity);
    }

    public void updateBlocking(Long id, Blocking blocking){
        BlockingEntity blockingEntity = blockingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("It does not exists a Block with id: " + id));
        // TODO: validar se a propriedade não está alugada nesse período
        // TODO: fazer o mapeamento
        blockingRepository.save(blockingEntity);
    }
}
