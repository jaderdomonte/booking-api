package com.hostfully.bookingapi.db.repository;

import com.hostfully.bookingapi.db.entity.BlockingEntity;
import com.hostfully.bookingapi.db.entity.BookingPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BlockingRepository extends JpaRepository<BlockingEntity, Long> {

    @Query("SELECT COUNT(b.id) " +
            "FROM BlockingEntity b " +
            "WHERE (" +
            "(b.period.checkIn <= :#{#period.checkIn} AND b.period.checkOut >= :#{#period.checkIn}) OR " +
            "(b.period.checkIn <= :#{#period.checkOut} AND b.period.checkOut >= :#{#period.checkOut}) OR" +
            "(b.period.checkIn >= :#{#period.checkIn} AND b.period.checkOut <= :#{#period.checkOut})" +
            ") " +
            "AND b.property.id = :propertyId")
    int checkOverlapping(@Param("propertyId") Long propertyId, @Param("period") BookingPeriod period);
}
