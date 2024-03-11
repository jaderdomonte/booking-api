package com.hostfully.bookingapi.infrastructure.db;

import com.hostfully.bookingapi.infrastructure.db.entity.*;
import com.hostfully.bookingapi.infrastructure.db.enumeration.BookingStatusEnum;
import com.hostfully.bookingapi.infrastructure.db.repository.BookingRepository;
import com.hostfully.bookingapi.infrastructure.db.repository.BookingStatusRepository;
import com.hostfully.bookingapi.infrastructure.db.repository.GuestRepository;
import com.hostfully.bookingapi.infrastructure.db.repository.PropertyRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final PropertyRepository propertyRepository;

    private final GuestRepository guestRepository;

    private final BookingStatusRepository bookingStatusRepository;

    private final BookingRepository bookingRepository;

    @Override
    public void run(String... args) throws Exception {
        createProperties();
        createGuests();
        createBookingStatus();
        createBookings();
    }

    private void createBookingStatus() {
        Arrays.stream(BookingStatusEnum.values())
                .forEach(statusEnum ->
                        bookingStatusRepository.save(new BookingStatusEntity(statusEnum.getId(), statusEnum.getDescription())));
    }

    private void createGuests() {
        GuestEntity aaronRodgers = new GuestEntity(new GuestName("Aaron", "Rodgers"));
        GuestEntity brockPurdy = new GuestEntity(new GuestName("Brock", "Purdy"));
        GuestEntity patrickMahomes = new GuestEntity(new GuestName("Patrick", "Mahomes"));
        GuestEntity jordanLove = new GuestEntity(new GuestName("Jordan", "Love"));
        GuestEntity ericStokes = new GuestEntity(new GuestName("Eric", "Stokes"));

        List<GuestEntity> guestEntities = Arrays.asList(aaronRodgers, brockPurdy, patrickMahomes, jordanLove, ericStokes);

        guestEntities.forEach(guestEntity -> guestRepository.save(guestEntity));
    }

    private void createProperties() {
        PropertyEntity lakefrontEscape = new PropertyEntity("Lakefront Escape");
        PropertyEntity beachsideHome = new PropertyEntity("Beachside Home");
        PropertyEntity villageCabin = new PropertyEntity("Village Cabin");
        PropertyEntity laPremiunSuite = new PropertyEntity("LA Premiun Suite");
        PropertyEntity snowLodge = new PropertyEntity("Snow Lodge");

        List<PropertyEntity> propertyEntities = Arrays.asList(lakefrontEscape, beachsideHome, villageCabin, laPremiunSuite, snowLodge);

        propertyEntities.forEach(propertyEntity -> propertyRepository.save(propertyEntity));

        System.out.println(propertyRepository.findAll().size());
    }

    private void createBookings() {
        GuestEntity guest = new GuestEntity(1L);
        BookingPeriod bookingPeriod = new BookingPeriod(LocalDate.now(), LocalDate.now().plusDays(10));
        BookingStatusEntity bookingStatusEntity = new BookingStatusEntity(BookingStatusEnum.CONFIRMED.getId());
        PropertyEntity propertyEntity = new PropertyEntity(1L, "Lakefront Escape");

        bookingRepository.save(new BookingEntity(guest, propertyEntity, bookingPeriod, bookingStatusEntity));

        GuestEntity guest2 = new GuestEntity(2L);
        BookingPeriod bookingPeriod2 = new BookingPeriod(LocalDate.now(), LocalDate.now().plusDays(10));
        PropertyEntity propertyEntity2 = new PropertyEntity(2L, "Beachside Home");

        bookingRepository.save(new BookingEntity(guest2, propertyEntity2, bookingPeriod2, bookingStatusEntity));
    }
}
