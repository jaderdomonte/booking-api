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
        LOG.info("Request received at getAllBookings"); // TODO: tomar como modelo

        List<Booking> allBookings = useCase.getAllBookings();
        List<BookingResponse> responseBody = allBookings.stream().map(booking -> mapper.fromDomainToResponse(booking)).toList();

        LOG.info("Request finished succesfully");
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id){
        LOG.info("Request received at getBookingById"); // TODO: tomar como modelo

        Booking booking = useCase.getBookingById(id);
        BookingResponse responseBody = mapper.fromDomainToResponse(booking);

        LOG.info("Request finished succesfully");
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping
    public ResponseEntity<Void> createBooking(@RequestBody @Valid BookingCreateRequest request){
        LOG.info("Request received at createBooking");

        Booking booking = mapper.fromRequestToDomain(request);
        useCase.createBooking(booking);

        LOG.info("Request finished succesfully");

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBooking(@PathVariable Long id, @RequestBody @Valid BookingUpdateRequest request){
        LOG.info("Request received at updateBooking");

        Booking booking = mapper.fromRequestToDomain(request);
        useCase.updateBooking(id, booking);

        LOG.info("Request finished succesfully");

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/cancel/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id){
        LOG.info("Request received at cancelBooking");

        useCase.cancelBooking(id);

        LOG.info("Request finished succesfully");

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/activate/{id}")
    public ResponseEntity<Void> activateBooking(@PathVariable Long id){
        LOG.info("Request received at activateBooking");

        useCase.activateBooking(id);

        LOG.info("Request finished succesfully");

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id){
        LOG.info("Request received at deleteBooking");

        useCase.deleteBooking(id);

        LOG.info("Request finished succesfully");

        return ResponseEntity.noContent().build();
    }
}
