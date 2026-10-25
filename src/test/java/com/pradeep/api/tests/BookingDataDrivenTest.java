package com.pradeep.api.tests;

import com.pradeep.api.base.BaseTest;
import com.pradeep.api.clients.BookingClient;
import com.pradeep.api.models.Booking;
import com.pradeep.api.utils.TestDataFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

/** Creates one booking per entry in testdata/bookings.json. Adding a new case needs no code change. */
public class BookingDataDrivenTest extends BaseTest {

    @DataProvider(name = "bookingsFromJson")
    public Object[][] bookingsFromJson() {
        Booking[] data = TestDataFactory.fromJson("testdata/bookings.json");
        Object[][] rows = new Object[data.length][1];
        for (int i = 0; i < data.length; i++) {
            rows[i][0] = data[i];
        }
        return rows;
    }

    @Test(dataProvider = "bookingsFromJson", description = "Create bookings from JSON test data")
    public void createBookingFromJson(Booking booking) {
        new BookingClient(requestSpec).createBooking(booking)
                .then()
                .statusCode(200)
                .body("booking.firstname", equalTo(booking.getFirstname()))
                .body("booking.lastname", equalTo(booking.getLastname()))
                .body("booking.totalprice", equalTo(booking.getTotalprice()))
                .body("booking.depositpaid", equalTo(booking.isDepositpaid()))
                .body("booking.additionalneeds", equalTo(booking.getAdditionalneeds()));
    }
}
