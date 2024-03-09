package com.hostfully.bookingapi.infrastructure.db.repository;

import com.hostfully.bookingapi.infrastructure.db.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, Long> {
}
