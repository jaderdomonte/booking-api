package com.hostfully.bookingapi.infrastructure.db.repository;

import com.hostfully.bookingapi.infrastructure.db.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
}
