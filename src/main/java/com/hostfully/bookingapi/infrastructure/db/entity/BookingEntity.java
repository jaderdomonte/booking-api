package com.hostfully.bookingapi.infrastructure.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "BOOKING")
@Getter
@Setter
@NoArgsConstructor
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private GuestEntity guest;

    @Embedded
    private BookingPeriod period;

    @ManyToOne
    private BookingStatusEntity status;

    public BookingEntity(GuestEntity guest, BookingPeriod period) {
        this.guest = guest;
        this.period = period;
    }
}
