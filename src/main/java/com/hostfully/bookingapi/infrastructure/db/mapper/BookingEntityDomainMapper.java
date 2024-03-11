package com.hostfully.bookingapi.infrastructure.db.mapper;

import com.hostfully.bookingapi.domain.*;
import com.hostfully.bookingapi.infrastructure.db.entity.*;
import com.hostfully.bookingapi.infrastructure.db.enumeration.BookingStatusEnum;
import org.springframework.stereotype.Component;

@Component
public class BookingEntityDomainMapper {

    public BookingEntity toEntity(Booking domain){
        GuestEntity guest = new GuestEntity(domain.getGuest().getId());
        BookingPeriod bookingPeriod = new BookingPeriod(domain.getPeriod().getCheckIn(), domain.getPeriod().getCheckOut());
        BookingStatusEntity bookingStatusEntity = new BookingStatusEntity(BookingStatusEnum.CONFIRMED.getId());
        PropertyEntity propertyEntity = new PropertyEntity(domain.getProperty().getId(), domain.getProperty().getName());

        return new BookingEntity(guest, propertyEntity, bookingPeriod, bookingStatusEntity);
    }

    public Booking toDomain(BookingEntity entity){
        GuestNameVO guestNameVO = new GuestNameVO(entity.getGuest().getFullName().getFirstName(), entity.getGuest().getFullName().getLastName());
        Guest guest = new Guest(entity.getGuest().getId(), guestNameVO);
        Property property = new Property(entity.getProperty().getId(), entity.getProperty().getName());
        BookingPeriodVO bookingPeriod = new BookingPeriodVO(entity.getPeriod().getCheckIn(), entity.getPeriod().getCheckOut());
        BookingStatusVO bookingStatusVO = new BookingStatusVO(entity.getStatus().getId(), entity.getStatus().getDescription());

        return new Booking(entity.getId(), guest, property, bookingPeriod, bookingStatusVO);
    }
}
