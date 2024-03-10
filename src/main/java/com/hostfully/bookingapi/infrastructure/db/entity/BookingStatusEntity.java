package com.hostfully.bookingapi.infrastructure.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "BOOKING_STATUS")
@Getter
@Setter
@NoArgsConstructor
public class BookingStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    public BookingStatusEntity(String description){
        this.description = description;
    }
}
