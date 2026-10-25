package com.pradeep.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** Response of POST /booking: { "bookingid": 1, "booking": { ... } } */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingResponse {

    private int bookingid;
    private Booking booking;

    public int getBookingid() { return bookingid; }
    public void setBookingid(int bookingid) { this.bookingid = bookingid; }
    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }
}
