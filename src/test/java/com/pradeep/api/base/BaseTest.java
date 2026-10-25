package com.pradeep.api.base;

import com.pradeep.api.utils.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeSuite;

import static org.hamcrest.Matchers.lessThan;

/** Shared request/response specifications so every test sends the same headers and checks the same basics. */
public abstract class BaseTest {

    protected static RequestSpecification requestSpec;
    protected static ResponseSpecification fastResponse;

    @BeforeSuite(alwaysRun = true)
    public void setUpSpecs() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        // REST Assured adds "; charset=ISO-8859-1" to Content-Type by default, which this API rejects.
        RestAssuredConfig config = RestAssured.config()
                .encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));

        requestSpec = new RequestSpecBuilder()
                .setConfig(config)
                .setBaseUri(ConfigManager.get("baseUri"))
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .log(LogDetail.URI)
                .build();

        fastResponse = new ResponseSpecBuilder()
                .expectResponseTime(lessThan(ConfigManager.getLong("maxResponseTimeMs")))
                .build();
    }
}
