package com.hostfully.bookingapi.web.controller;

import com.hostfully.bookingapi.domain.Booking;
import com.hostfully.bookingapi.usecases.BookingUseCase;
import com.hostfully.bookingapi.web.mapper.BookingDtoDomainMapper;
import com.hostfully.bookingapi.web.request.BookingCreateRequest;
import com.hostfully.bookingapi.web.request.BookingUpdateRequest;
import com.hostfully.bookingapi.web.response.BookingResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@AllArgsConstructor
public class BookingController {

    private static final Logger LOG = LoggerFactory.getLogger(BookingController.class);

    private final BookingUseCase useCase;

    private final BookingDtoDomainMapper mapper;

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings(){
        LOG.info("Receiving a GET to return all Bookings");

        List<Booking> allBookings = useCase.getAllBookings();
        List<BookingResponse> responseBody = allBookings.stream().map(booking -> mapper.fromDomainToResponse(booking)).toList();

        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id){
        LOG.info("Receiving a GET to return Booking with id {}", id);

        Booking booking = useCase.getBookingById(id);
        BookingResponse responseBody = mapper.fromDomainToResponse(booking);

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping
    public ResponseEntity<Void> createBooking(@RequestBody @Valid BookingCreateRequest request){
        LOG.info("Receiving a POST to create Booking");

        Booking booking = mapper.fromRequestToDomain(request);
        useCase.createBooking(booking);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBooking(@PathVariable Long id, @RequestBody @Valid BookingUpdateRequest request){
        LOG.info("Receiving a PUT to update Booking with id {}", id);

        Booking booking = mapper.fromRequestToDomain(request);
        useCase.updateBooking(id, booking);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/cancel/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id){
        LOG.info("Receiving a PATCH to cancel Booking with id {}", id);

        useCase.cancelBooking(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/activate/{id}")
    public ResponseEntity<Void> activateBooking(@PathVariable Long id){
        LOG.info("Receiving a PATCH to activate Booking with id {}", id);

        useCase.activateBooking(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id){
        LOG.info("Receiving a DELETE to delete Booking with id {}", id);

        useCase.deleteBooking(id);

        return ResponseEntity.noContent().build();
    }
}
