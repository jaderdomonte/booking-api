package com.hostfully.bookingapi.infrastructure.db.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "BOOKING_STATUS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingStatusEntity {

    @Id
    private Long id;

    private String description;

    public BookingStatusEntity(Long id) {
        this.id = id;
    }

    public BookingStatusEntity(String description){
        this.description = description;
    }
}
