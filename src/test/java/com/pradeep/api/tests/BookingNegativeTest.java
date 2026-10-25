package com.pradeep.api.tests;

import com.pradeep.api.base.BaseTest;
import com.pradeep.api.clients.BookingClient;
import com.pradeep.api.utils.TestDataFactory;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

/** Checks the API refuses bad requests. These are the tests that usually find real bugs. */
public class BookingNegativeTest extends BaseTest {

    @Test(description = "GET a booking id that does not exist returns 404")
    public void nonExistingBookingReturns404() {
        new BookingClient(requestSpec).getBooking(99999999).then().statusCode(404);
    }

    @Test(description = "PUT without a token returns 403 Forbidden")
    public void updateWithoutTokenIsForbidden() {
        given().spec(requestSpec)
                .body(TestDataFactory.randomBooking())
                .when().put("/booking/1")
                .then().statusCode(403);
    }

    @Test(description = "DELETE with an invalid token returns 403 Forbidden")
    public void deleteWithInvalidTokenIsForbidden() {
        new BookingClient(requestSpec).deleteBooking(1, "invalid-token").then().statusCode(403);
    }

    @Test(description = "POST with missing required fields returns 500 (known API behaviour, should ideally be 400)")
    public void createWithMissingFieldsFails() {
        Map<String, Object> incomplete = new HashMap<>();
        incomplete.put("firstname", "OnlyName");

        given().spec(requestSpec)
                .body(incomplete)
                .when().post("/booking")
                .then().statusCode(500);
    }
}
