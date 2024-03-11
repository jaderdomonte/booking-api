package com.hostfully.bookingapi.web.mapper;

import com.hostfully.bookingapi.domain.*;
import com.hostfully.bookingapi.infrastructure.db.enumeration.BookingStatusEnum;
import com.hostfully.bookingapi.web.dto.BookingPeriodDto;
import com.hostfully.bookingapi.web.dto.GuestDto;
import com.hostfully.bookingapi.web.dto.PropertyDto;
import com.hostfully.bookingapi.web.request.BookingCreateRequest;
import com.hostfully.bookingapi.web.request.BookingUpdateRequest;
import com.hostfully.bookingapi.web.response.BookingResponse;
import org.springframework.stereotype.Component;

@Component
public class BookingDtoDomainMapper {

    public BookingResponse fromDomainToResponse(Booking domain){
        GuestDto guestDto = new GuestDto(domain.getGuest().getId(), domain.getGuest().getFullName().getFirstName(), domain.getGuest().getFullName().getLastName());
        BookingPeriodDto bookingPeriodDto = new BookingPeriodDto(domain.getPeriod().getCheckIn(), domain.getPeriod().getCheckOut());
        PropertyDto propertyDto = new PropertyDto(domain.getProperty().getId(), domain.getProperty().getName());
        return new BookingResponse(domain.getId(), guestDto, propertyDto, bookingPeriodDto, domain.getStatus().getDescription());
    }

    public Booking fromRequestToDomain(BookingCreateRequest request){
        Guest guest = new Guest(request.guestId());
        Property property = new Property(request.propertyId());
        BookingPeriodVO bookingPeriod = new BookingPeriodVO(request.checkIn(), request.checkOut());
        BookingStatusVO bookingStatusVO = new BookingStatusVO(BookingStatusEnum.CONFIRMED.getId(), BookingStatusEnum.CONFIRMED.getDescription());
        return new Booking(guest, property, bookingPeriod, bookingStatusVO);
    }

    public Booking fromRequestToDomain(BookingUpdateRequest request){
        Guest guest = new Guest(request.guestId());
        BookingPeriodVO bookingPeriod = new BookingPeriodVO(request.checkIn(), request.checkOut());
        return new Booking(guest, bookingPeriod);
    }
}
