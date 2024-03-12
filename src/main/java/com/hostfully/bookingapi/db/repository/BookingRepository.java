package com.hostfully.bookingapi.db.repository;

import com.hostfully.bookingapi.db.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    Optional<BookingEntity> findByIdAndStatusId(Long id, Long statusId);

    @Modifying
    @Query("UPDATE BookingEntity b SET b.status.id = :statusId WHERE b.id = :id")
    void changeBookingStatus(@Param("id") Long id, @Param("statusId") Long statusId);

    @Query("SELECT COUNT(b.id) " +
            "FROM BookingEntity b " +
            "WHERE ((b.period.checkIn <= :checkIn AND b.period.checkOut >= :checkIn) OR (b.period.checkIn <= :checkOut AND b.period.checkOut >= :checkIn))" +
            "AND b.status.id = 1")
    int checkOverlapping(@Param("checkIn") LocalDate checkIn, @Param("checkOut") LocalDate checkOut);

    @Query("FROM BookingEntity b " +
            "WHERE ((b.period.checkIn <= :checkIn AND b.period.checkOut >= :checkIn) OR (b.period.checkIn <= :checkOut AND b.period.checkOut >= :checkIn))" +
            "AND b.status.id = 1")
    List<BookingEntity> checkOverlappingBookings(@Param("checkIn") LocalDate checkIn, @Param("checkOut") LocalDate checkOut);
}
