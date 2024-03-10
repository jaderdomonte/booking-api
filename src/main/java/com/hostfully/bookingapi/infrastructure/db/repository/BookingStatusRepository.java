package com.hostfully.bookingapi.infrastructure.db.repository;

import com.hostfully.bookingapi.infrastructure.db.entity.BookingStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingStatusRepository extends JpaRepository<BookingStatusEntity, Long> {
}
