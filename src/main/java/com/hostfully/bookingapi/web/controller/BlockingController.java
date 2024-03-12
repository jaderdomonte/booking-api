package com.hostfully.bookingapi.web.controller;

import com.hostfully.bookingapi.domain.Blocking;
import com.hostfully.bookingapi.usecases.BlockingUseCase;
import com.hostfully.bookingapi.web.dto.BookingPeriodDto;
import com.hostfully.bookingapi.web.mapper.BlockingDtoDomainMapper;
import com.hostfully.bookingapi.web.request.BlockingCreateRequest;
import com.hostfully.bookingapi.web.request.BlockingUpdateRequest;
import com.hostfully.bookingapi.web.response.BlockingResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blockings")
@AllArgsConstructor
public class BlockingController {

    private static final Logger LOG = LoggerFactory.getLogger(BlockingController.class);

    private final BlockingUseCase useCase;

    private final BlockingDtoDomainMapper mapper;

    @GetMapping
    public ResponseEntity<List<BlockingResponse>> getAllBlockings(){
        LOG.info("Receiving a GET to return all Blockings");
        List<Blocking> blockingsDomain = useCase.getAllBlockings();
        List<BlockingResponse> responseBody = blockingsDomain.stream().map(blocking -> mapper.fromDomainToResponse(blocking)).toList();

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping
    public ResponseEntity<Void> createBlocking(@RequestBody @Valid BlockingCreateRequest request){
        LOG.info("Receiving a POST to create Blocking");
        Blocking blocking = mapper.fromRequestToDomain(request);
        useCase.createBlocking(blocking);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBlocking(@PathVariable Long id, @Valid @RequestBody BlockingUpdateRequest request){
        LOG.info("Receiving a PUT to update Blocking with id {}", id);
        BookingPeriodDto bookingPeriod = mapper.fromRequestToDomain(request);

        useCase.updateBlocking(id, bookingPeriod);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlocking(@PathVariable Long id){
        LOG.info("Receiving a DELETE to delete Blocking with id {}", id);
        useCase.deleteBlocking(id);

        return ResponseEntity.noContent().build();
    }
}
