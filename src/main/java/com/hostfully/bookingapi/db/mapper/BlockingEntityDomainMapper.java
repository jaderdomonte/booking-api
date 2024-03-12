package com.hostfully.bookingapi.db.mapper;

import com.hostfully.bookingapi.db.entity.BlockingEntity;
import com.hostfully.bookingapi.db.entity.BookingPeriod;
import com.hostfully.bookingapi.db.entity.PropertyEntity;
import com.hostfully.bookingapi.domain.Blocking;
import com.hostfully.bookingapi.domain.Property;
import org.springframework.stereotype.Component;

@Component
public class BlockingEntityDomainMapper {

    public Blocking toDomain(BlockingEntity entity){
        BookingPeriod bookingPeriod = BookingPeriod.builder().checkIn(entity.getPeriod().getCheckIn()).checkOut(entity.getPeriod().getCheckOut()).build();
        Property property = new Property(entity.getProperty().getId(), entity.getProperty().getName());
        return new Blocking(entity.getId(), property, bookingPeriod);
    }

    public BlockingEntity toEntity(Blocking domain){
        BookingPeriod bookingPeriod = BookingPeriod.builder().checkIn(domain.getPeriod().getCheckIn()).checkOut(domain.getPeriod().getCheckOut()).build();
        PropertyEntity propertyEntity = PropertyEntity.builder().id(domain.getProperty().getId()).name(domain.getProperty().getName()).build();
        return BlockingEntity.builder().property(propertyEntity).period(bookingPeriod).build();
    }
}
