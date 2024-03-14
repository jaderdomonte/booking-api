package com.hostfully.bookingapi.db.repository;

import com.hostfully.bookingapi.db.entity.BookingEntity;
import com.hostfully.bookingapi.db.entity.BookingPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    Optional<BookingEntity> findByIdAndStatusId(Long id, Long statusId);

    @Modifying
    @Query("UPDATE BookingEntity b SET b.status.id = :statusId WHERE b.id = :id")
    void changeBookingStatus(@Param("id") Long id, @Param("statusId") Long statusId);

    @Query("SELECT COUNT(b.id) " +
            "FROM BookingEntity b " +
            "WHERE ( " +
            "(b.period.checkIn <= :#{#period.checkIn} AND b.period.checkOut > :#{#period.checkIn}) OR " +
            "(b.period.checkIn < :#{#period.checkOut} AND b.period.checkOut >= :#{#period.checkOut}) OR " +
            "(b.period.checkIn >= :#{#period.checkIn} AND b.period.checkOut <= :#{#period.checkOut})" +
            ") " +
            "AND b.status.id = 1 " +
            "AND b.property.id = :propertyId")
    int checkOverlapping(@Param("propertyId") Long propertyId, @Param("period") BookingPeriod period);

    @Query("SELECT COUNT(b.id) " +
            "FROM BookingEntity b " +
            "WHERE ( " +
            "(b.period.checkIn <= :#{#period.checkIn} AND b.period.checkOut > :#{#period.checkIn}) OR " +
            "(b.period.checkIn < :#{#period.checkOut} AND b.period.checkOut >= :#{#period.checkOut}) OR " +
            "(b.period.checkIn >= :#{#period.checkIn} AND b.period.checkOut <= :#{#period.checkOut})" +
            ") " +
            "AND b.status.id = 1 " +
            "AND b.property.id = :propertyId " +
            "AND b.id <> :id")
    int checkOverlapping(@Param("id") Long id, @Param("propertyId") Long propertyId, @Param("period") BookingPeriod period);

}
