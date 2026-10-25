package com.pradeep.api.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pradeep.api.models.Booking;
import com.pradeep.api.models.BookingDates;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Random;

/** Creates fresh test data so tests never depend on each other's data. */
public final class TestDataFactory {

    private static final Random RANDOM = new Random();
    private static final String[] FIRST_NAMES = {"Arjun", "Priya", "Rahul", "Sneha", "Kiran", "Divya"};
    private static final String[] LAST_NAMES  = {"Sharma", "Reddy", "Kumar", "Iyer", "Naidu", "Patel"};

    private TestDataFactory() { }

    public static Booking randomBooking() {
        LocalDate checkin = LocalDate.now().plusDays(1 + RANDOM.nextInt(30));
        LocalDate checkout = checkin.plusDays(1 + RANDOM.nextInt(7));
        return new Booking(
                FIRST_NAMES[RANDOM.nextInt(FIRST_NAMES.length)],
                LAST_NAMES[RANDOM.nextInt(LAST_NAMES.length)],
                100 + RANDOM.nextInt(900),
                RANDOM.nextBoolean(),
                new BookingDates(checkin.toString(), checkout.toString()),
                "Breakfast");
    }

    /** Reads an array of bookings from a JSON file in src/test/resources. */
    public static Booking[] fromJson(String resourcePath) {
        try (InputStream in = TestDataFactory.class.getClassLoader().getResourceAsStream(resourcePath)) {
            return new ObjectMapper().readValue(in, Booking[].class);
        } catch (Exception e) {
            throw new RuntimeException("Could not read " + resourcePath, e);
        }
    }
}
