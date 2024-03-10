package com.hostfully.bookingapi.infrastructure.db;

import com.hostfully.bookingapi.infrastructure.db.entity.BookingStatusEntity;
import com.hostfully.bookingapi.infrastructure.db.entity.GuestEntity;
import com.hostfully.bookingapi.infrastructure.db.entity.GuestName;
import com.hostfully.bookingapi.infrastructure.db.entity.PropertyEntity;
import com.hostfully.bookingapi.infrastructure.db.repository.BookingStatusRepository;
import com.hostfully.bookingapi.infrastructure.db.repository.GuestRepository;
import com.hostfully.bookingapi.infrastructure.db.repository.PropertyRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final PropertyRepository propertyRepository;

    private final GuestRepository guestRepository;

    private final BookingStatusRepository bookingStatusRepository;

    @Override
    public void run(String... args) throws Exception {
        createProperties();
        createGuests();
        createBookingStatus();
    }

    private void createBookingStatus() {
        BookingStatusEntity confirmed = new BookingStatusEntity("CONFIRMED");
        BookingStatusEntity canceled = new BookingStatusEntity("CANCELED");

        List<BookingStatusEntity> bookingStatusEntities = Arrays.asList(confirmed, canceled);

        bookingStatusEntities.forEach(status -> bookingStatusRepository.save(status));
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
}
