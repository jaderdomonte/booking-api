package com.hostfully.bookingapi.infrastructure.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "BLOCKING")
@Getter
@Setter
@NoArgsConstructor
public class BlockingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private PropertyEntity property;

    @Embedded
    private BookingPeriod period;

    public BlockingEntity(PropertyEntity property, BookingPeriod period) {
        this.property = property;
        this.period = period;
    }
}
