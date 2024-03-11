package com.hostfully.bookingapi.web.controller;

import com.hostfully.bookingapi.domain.Booking;
import com.hostfully.bookingapi.usecases.BookingUseCase;
import com.hostfully.bookingapi.web.mapper.BookingDtoDomainMapper;
import com.hostfully.bookingapi.web.request.BookingCreateRequest;
import com.hostfully.bookingapi.web.request.BookingUpdateRequest;
import com.hostfully.bookingapi.web.response.BookingResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bookings")
@AllArgsConstructor
public class BookingController {

    private final BookingUseCase useCase;

    private final BookingDtoDomainMapper mapper;

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings(){
        List<Booking> allBookings = useCase.getAllBookings();
        List<BookingResponse> responseBody = allBookings.stream().map(booking -> mapper.fromDomainToResponse(booking)).toList();

        log.info("Request succesfull. Returned {} Bookings.", responseBody.size());

        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id){
        log.debug("Request received with id {}", id);

        Booking booking = useCase.getBookingById(id);
        BookingResponse responseBody = mapper.fromDomainToResponse(booking);

        log.info("Request succesfull. Returned payload {}", responseBody);

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping
    public ResponseEntity<Void> createBooking(@RequestBody @Valid BookingCreateRequest request){
        log.info("Request received with body {}", request);

        Booking booking = mapper.fromRequestToDomain(request);
        useCase.createBooking(booking);

        log.info("Request succesfull.");

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatBooking(@PathVariable Long id, @RequestBody @Valid BookingUpdateRequest request){
        log.info("Request received with body {}", request);

        Booking booking = mapper.fromRequestToDomain(request);
        useCase.updateBooking(id, booking);

        log.info("Request succesfull.");

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/cancel/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id){
        log.debug("Request received with id {}", id);

        useCase.cancelBooking(id);

        log.info("Request succesfull.");

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/activate/{id}")
    public ResponseEntity<Void> activateBooking(@PathVariable Long id){
        log.debug("Request received with id {}", id);

        useCase.activateBooking(id);

        log.info("Request succesfull.");

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id){
        log.debug("Request received with id {}", id);

        useCase.deleteBooking(id);

        log.info("Request succesfull.");

        return ResponseEntity.noContent().build();
    }
}
