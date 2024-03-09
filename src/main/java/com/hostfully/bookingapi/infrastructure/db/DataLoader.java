package com.hostfully.bookingapi.infrastructure.db;

import com.hostfully.bookingapi.infrastructure.db.entity.Guest;
import com.hostfully.bookingapi.infrastructure.db.entity.GuestName;
import com.hostfully.bookingapi.infrastructure.db.entity.PropertyEntity;
import com.hostfully.bookingapi.infrastructure.db.repository.BookingStatus;
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

    private PropertyRepository propertyRepository;

    private GuestRepository guestRepository;

    private BookingStatusRepository bookingStatusRepository;

    @Override
    public void run(String... args) throws Exception {
        createProperties();
        createGuests();
        createBookingStatus();
    }

    private void createBookingStatus() {
        BookingStatus confirmed = new BookingStatus("CONFIRMED");
        BookingStatus canceled = new BookingStatus("CANCELED");

        List<BookingStatus> bookingStatus = Arrays.asList(confirmed, canceled);

        bookingStatus.forEach(status -> bookingStatusRepository.save(status));
    }

    private void createGuests() {
        Guest aaronRodgers = new Guest(new GuestName("Aaron", "Rodgers"));
        Guest brockPurdy = new Guest(new GuestName("Brock", "Purdy"));
        Guest patrickMahomes = new Guest(new GuestName("Patrick", "Mahomes"));
        Guest jordanLove = new Guest(new GuestName("Jordan", "Love"));
        Guest ericStokes = new Guest(new GuestName("Eric", "Stokes"));

        List<Guest> guests = Arrays.asList(aaronRodgers, brockPurdy, patrickMahomes, jordanLove, ericStokes);

        guests.forEach(guest -> guestRepository.save(guest));
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
