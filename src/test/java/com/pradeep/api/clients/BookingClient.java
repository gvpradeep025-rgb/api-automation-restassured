package com.pradeep.api.clients;

import com.pradeep.api.models.Booking;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

/** One method per /booking endpoint. Tests call these instead of writing raw HTTP calls (like Page Objects for APIs). */
public class BookingClient {

    private final RequestSpecification spec;

    public BookingClient(RequestSpecification spec) {
        this.spec = spec;
    }

    public Response getAllBookings() {
        return given().spec(spec).when().get("/booking");
    }

    public Response getBookingsFiltered(Map<String, ?> queryParams) {
        return given().spec(spec).queryParams(queryParams).when().get("/booking");
    }

    public Response getBooking(int id) {
        return given().spec(spec).pathParam("id", id).when().get("/booking/{id}");
    }

    public Response createBooking(Booking booking) {
        return given().spec(spec).body(booking).when().post("/booking");
    }

    public Response updateBooking(int id, Booking booking, String token) {
        return given().spec(spec).cookie("token", token).pathParam("id", id)
                .body(booking).when().put("/booking/{id}");
    }

    public Response partialUpdateBooking(int id, Map<String, Object> fields, String token) {
        return given().spec(spec).cookie("token", token).pathParam("id", id)
                .body(fields).when().patch("/booking/{id}");
    }

    public Response deleteBooking(int id, String token) {
        return given().spec(spec).cookie("token", token).pathParam("id", id)
                .when().delete("/booking/{id}");
    }

    public Response ping() {
        return given().spec(spec).when().get("/ping");
    }
}
