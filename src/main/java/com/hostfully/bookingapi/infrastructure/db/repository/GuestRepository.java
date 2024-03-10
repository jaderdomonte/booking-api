package com.hostfully.bookingapi.infrastructure.db.repository;

import com.hostfully.bookingapi.infrastructure.db.entity.GuestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<GuestEntity, Long> {
}
