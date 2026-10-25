package com.pradeep.api.tests;

import com.pradeep.api.base.BaseTest;
import com.pradeep.api.clients.AuthClient;
import com.pradeep.api.clients.BookingClient;
import com.pradeep.api.models.Booking;
import com.pradeep.api.models.BookingResponse;
import com.pradeep.api.utils.ConfigManager;
import com.pradeep.api.utils.TestDataFactory;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

/**
 * End-to-end flow: Create -> Read -> Update -> Partial update -> Delete -> Verify deleted.
 * Each step uses the booking id created in step 1 (request chaining).
 */
public class BookingCrudTest extends BaseTest {

    private BookingClient bookings;
    private String token;
    private int bookingId;
    private Booking created;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        bookings = new BookingClient(requestSpec);
        token = new AuthClient(requestSpec).getToken(ConfigManager.get("username"), ConfigManager.get("password"));
    }

    @Test(priority = 1, groups = {"smoke"}, description = "POST /booking creates a booking")
    public void createBooking() {
        created = TestDataFactory.randomBooking();

        BookingResponse response = bookings.createBooking(created)
                .then()
                .spec(fastResponse)
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/create-booking-schema.json"))
                .extract().as(BookingResponse.class);

        bookingId = response.getBookingid();
        Assert.assertTrue(bookingId > 0, "Booking id should be positive");
        Assert.assertEquals(response.getBooking().getFirstname(), created.getFirstname());
        Assert.assertEquals(response.getBooking().getTotalprice(), created.getTotalprice());
        Assert.assertEquals(response.getBooking().getBookingdates().getCheckin(), created.getBookingdates().getCheckin());
    }

    @Test(priority = 2, dependsOnMethods = "createBooking", description = "GET /booking/{id} returns the created booking")
    public void getBooking() {
        bookings.getBooking(bookingId)
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/booking-schema.json"))
                .body("firstname", equalTo(created.getFirstname()))
                .body("lastname", equalTo(created.getLastname()))
                .body("depositpaid", equalTo(created.isDepositpaid()));
    }

    @Test(priority = 3, dependsOnMethods = "createBooking", description = "PUT /booking/{id} replaces the booking")
    public void updateBooking() {
        Booking updated = TestDataFactory.randomBooking();
        updated.setFirstname("Updated");
        updated.setAdditionalneeds("Lunch");

        bookings.updateBooking(bookingId, updated, token)
                .then()
                .statusCode(200)
                .body("firstname", equalTo("Updated"))
                .body("additionalneeds", equalTo("Lunch"))
                .body("totalprice", equalTo(updated.getTotalprice()));
    }

    @Test(priority = 4, dependsOnMethods = "updateBooking", description = "PATCH /booking/{id} changes only the given fields")
    public void partialUpdateBooking() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("lastname", "Patched");
        fields.put("totalprice", 999);

        bookings.partialUpdateBooking(bookingId, fields, token)
                .then()
                .statusCode(200)
                .body("lastname", equalTo("Patched"))
                .body("totalprice", equalTo(999))
                .body("firstname", equalTo("Updated"));   // unchanged from the PUT step
    }

    @Test(priority = 5, dependsOnMethods = "partialUpdateBooking", description = "DELETE /booking/{id} removes the booking")
    public void deleteBooking() {
        bookings.deleteBooking(bookingId, token).then().statusCode(201);
    }

    @Test(priority = 6, dependsOnMethods = "deleteBooking", description = "Deleted booking returns 404")
    public void deletedBookingIsGone() {
        bookings.getBooking(bookingId).then().statusCode(404);
    }
}
