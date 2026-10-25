package com.pradeep.api.clients;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

/** Calls POST /auth. */
public class AuthClient {

    private final RequestSpecification spec;

    public AuthClient(RequestSpecification spec) {
        this.spec = spec;
    }

    public Response createToken(String username, String password) {
        Map<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("password", password);
        return given().spec(spec).body(body).when().post("/auth");
    }

    public String getToken(String username, String password) {
        return createToken(username, password).then().statusCode(200).extract().path("token");
    }
}
