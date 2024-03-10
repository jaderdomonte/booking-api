package com.hostfully.bookingapi.web.mapper;

import com.hostfully.bookingapi.domain.Blocking;
import com.hostfully.bookingapi.domain.Property;
import com.hostfully.bookingapi.infrastructure.db.entity.BookingPeriod;
import com.hostfully.bookingapi.web.dto.BookingPeriodDto;
import com.hostfully.bookingapi.web.dto.PropertyDto;
import com.hostfully.bookingapi.web.request.BlockingCreateRequest;
import com.hostfully.bookingapi.web.response.BlockingResponse;
import org.springframework.stereotype.Component;

@Component
public class BlockingDtoDomainMapper {

    public BlockingResponse fromDomainToResponse(Blocking domain){
        PropertyDto property = new PropertyDto(domain.getProperty().getId(), domain.getProperty().getName());
        BookingPeriodDto bookingPeriodDto = new BookingPeriodDto(domain.getPeriod().getCheckIn(), domain.getPeriod().getCheckOut());
        return new BlockingResponse(domain.getId(), property, bookingPeriodDto);
    }

    public Blocking fromRequestToDomain(BlockingCreateRequest request){
        Property property = new Property(request.propertyId(), null);
        BookingPeriod bookingPeriod = new BookingPeriod(request.checkIn(), request.checkOut());
        return new Blocking(property, bookingPeriod);
    }
}
