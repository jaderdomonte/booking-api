package com.hostfully.bookingapi.infrastructure.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "BOOKING")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private GuestEntity guest;

    @ManyToOne
    private PropertyEntity property;

    @Embedded
    private BookingPeriod period;

    @ManyToOne
    private BookingStatusEntity status;

    public BookingEntity(GuestEntity guest, PropertyEntity property, BookingPeriod period, BookingStatusEntity status) {
        this.guest = guest;
        this.property = property;
        this.period = period;
        this.status = status;
    }
}
