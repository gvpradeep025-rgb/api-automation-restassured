package com.pradeep.api.tests;

import com.pradeep.api.base.BaseTest;
import com.pradeep.api.clients.AuthClient;
import com.pradeep.api.utils.ConfigManager;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class AuthTest extends BaseTest {

    @Test(groups = {"smoke"}, description = "Valid credentials return a token")
    public void validCredentialsReturnToken() {
        new AuthClient(requestSpec)
                .createToken(ConfigManager.get("username"), ConfigManager.get("password"))
                .then()
                .spec(fastResponse)
                .statusCode(200)
                .body("token", not(emptyOrNullString()))
                .body("token.length()", greaterThan(10));
    }

    @Test(description = "Wrong password returns 'Bad credentials' and no token")
    public void wrongPasswordIsRejected() {
        new AuthClient(requestSpec)
                .createToken(ConfigManager.get("username"), "wrong-password")
                .then()
                .statusCode(200)                      // this API returns 200 even for bad credentials
                .body("reason", equalTo("Bad credentials"))
                .body("token", nullValue());
    }
}
