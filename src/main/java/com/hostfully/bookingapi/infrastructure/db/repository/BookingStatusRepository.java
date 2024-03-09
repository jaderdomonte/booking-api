package com.hostfully.bookingapi.infrastructure.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingStatusRepository extends JpaRepository<BookingStatus, Long> {
}
