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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blockings")
@AllArgsConstructor
public class BlockingController {

    private final BlockingUseCase useCase;

    private final BlockingDtoDomainMapper mapper;

    @GetMapping
    public ResponseEntity<List<BlockingResponse>> getAllBlockings(){
        List<Blocking> blockingsDomain = useCase.getAllBlockings();
        List<BlockingResponse> responseBody = blockingsDomain.stream().map(blocking -> mapper.fromDomainToResponse(blocking)).toList();

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping
    public ResponseEntity<Void> createBlocking(@RequestBody @Valid BlockingCreateRequest request){
        Blocking blocking = mapper.fromRequestToDomain(request);
        useCase.createBlocking(blocking);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBlocking(@PathVariable Long id, @Valid @RequestBody BlockingUpdateRequest request){
        BookingPeriodDto bookingPeriod = mapper.fromRequestToDomain(request);

        useCase.updateBlocking(id, bookingPeriod);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlocking(@PathVariable Long id){
        useCase.deleteBlocking(id);

        return ResponseEntity.noContent().build();
    }
}
