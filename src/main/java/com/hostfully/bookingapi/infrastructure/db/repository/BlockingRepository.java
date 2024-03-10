package com.hostfully.bookingapi.infrastructure.db.repository;

import com.hostfully.bookingapi.infrastructure.db.entity.BlockingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockingRepository extends JpaRepository<BlockingEntity, Long> {
}
