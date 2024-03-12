package com.hostfully.bookingapi.db.repository;

import com.hostfully.bookingapi.db.entity.BlockingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BlockingRepository extends JpaRepository<BlockingEntity, Long> {

    @Query("SELECT COUNT(b.id) " +
            "FROM BlockingEntity b " +
            "WHERE ((b.period.checkIn <= :checkIn AND b.period.checkOut >= :checkIn) " +
            "OR (b.period.checkIn <= :checkOut AND b.period.checkOut >= :checkIn))")
    int checkOverlapping(@Param("checkIn") LocalDate checkIn, @Param("checkOut") LocalDate checkOut);

    @Query("FROM BlockingEntity b " +
            "WHERE ((b.period.checkIn <= :checkIn AND b.period.checkOut >= :checkIn) " +
            "OR (b.period.checkIn <= :checkOut AND b.period.checkOut >= :checkIn))")
    List<BlockingEntity> checkOverlappingBlockings(@Param("checkIn") LocalDate checkIn, @Param("checkOut") LocalDate checkOut);
}
