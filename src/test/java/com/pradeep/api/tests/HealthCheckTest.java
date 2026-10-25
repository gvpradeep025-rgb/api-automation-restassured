package com.pradeep.api.tests;

import com.pradeep.api.base.BaseTest;
import com.pradeep.api.clients.BookingClient;
import org.testng.annotations.Test;

public class HealthCheckTest extends BaseTest {

    @Test(groups = {"smoke"}, description = "API is up: GET /ping returns 201")
    public void apiIsUp() {
        new BookingClient(requestSpec).ping().then().statusCode(201);
    }
}
