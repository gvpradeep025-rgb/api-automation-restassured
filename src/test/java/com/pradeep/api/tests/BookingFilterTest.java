package com.pradeep.api.tests;

import com.pradeep.api.base.BaseTest;
import com.pradeep.api.clients.BookingClient;
import com.pradeep.api.models.Booking;
import com.pradeep.api.utils.TestDataFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

/** GET /booking with query parameters. */
public class BookingFilterTest extends BaseTest {

    private BookingClient bookings;
    private int bookingId;
    private Booking booking;

    @BeforeClass(alwaysRun = true)
    public void createBookingToSearchFor() {
        bookings = new BookingClient(requestSpec);
        booking = TestDataFactory.randomBooking();
        booking.setFirstname("Filtertest");
        bookingId = bookings.createBooking(booking).then().statusCode(200).extract().path("bookingid");
    }

    @Test(groups = {"smoke"}, description = "GET /booking returns a non-empty list of ids")
    public void getAllBookingsReturnsIds() {
        bookings.getAllBookings()
                .then()
                .spec(fastResponse)
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].bookingid", notNullValue());
    }

    @Test(description = "Filter by first and last name finds the booking just created")
    public void filterByNameFindsBooking() {
        Map<String, Object> params = new HashMap<>();
        params.put("firstname", booking.getFirstname());
        params.put("lastname", booking.getLastname());

        bookings.getBookingsFiltered(params)
                .then()
                .statusCode(200)
                .body("bookingid", hasItem(bookingId));
    }
}
