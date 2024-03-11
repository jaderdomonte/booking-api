package com.hostfully.bookingapi.infrastructure.db.mapper;

import com.hostfully.bookingapi.domain.Blocking;
import com.hostfully.bookingapi.domain.Property;
import com.hostfully.bookingapi.infrastructure.db.entity.BlockingEntity;
import com.hostfully.bookingapi.infrastructure.db.entity.BookingPeriod;
import com.hostfully.bookingapi.infrastructure.db.entity.PropertyEntity;
import org.springframework.stereotype.Component;

@Component
public class BlockingEntityDomainMapper {

    public Blocking toDomain(BlockingEntity entity){
        BookingPeriod bookingPeriod = new BookingPeriod(entity.getPeriod().getCheckIn(), entity.getPeriod().getCheckOut());
        Property property = new Property(entity.getProperty().getId(), entity.getProperty().getName());
        return new Blocking(entity.getId(), property, bookingPeriod);
    }

    public BlockingEntity toEntity(Blocking domain){
        BookingPeriod bookingPeriod = new BookingPeriod(domain.getPeriod().getCheckIn(), domain.getPeriod().getCheckOut());
        PropertyEntity propertyEntity = new PropertyEntity(domain.getProperty().getId(), domain.getProperty().getName());
        return new BlockingEntity(propertyEntity, bookingPeriod);
    }
}
