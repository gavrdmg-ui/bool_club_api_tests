package tests;

import models.login.LoginBodyModel;
import models.logout.LogoutBodyModel;
import models.logout.LogoutWithoutOrBlankTokenResponseModel;
import org.junit.jupiter.api.Test;
import testdata.BaseTestData;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.login.LoginSpec.loginRequestSpec;
import static specs.login.LoginSpec.successfulLoginResponseSpec;
import static specs.logout.LogoutSpec.*;


public class LogoutTests extends TestBase {

    @Test
    public void successfulLogoutTest() {
        LoginBodyModel loginData = new LoginBodyModel(BaseTestData.username, BaseTestData.password);

        String refreshToken = given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract().path("refresh");

        LogoutBodyModel logoutData = new LogoutBodyModel(refreshToken);

        given(logoutRequestSpec)
                .body(logoutData)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(successfulLogoutResponseSpec);
    }

    @Test
    public void logoutWithBlankToken() {
        LogoutBodyModel logoutData = new LogoutBodyModel("");

        LogoutWithoutOrBlankTokenResponseModel registrationResponse = given()
                .spec(logoutRequestSpec)
                .body(logoutData)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(logoutWithBlankTokenResponseSpec).extract()
                .as(LogoutWithoutOrBlankTokenResponseModel.class);

        String tokenActualDetailError = registrationResponse.refresh().get(0);

        assertThat(tokenActualDetailError).isEqualTo(BaseTestData.expectedBlankError);
    }

    @Test
    public void logoutWithoutToken() {

        LogoutWithoutOrBlankTokenResponseModel registrationResponse = given()
                .spec(logoutRequestSpec)
                .body("{}")
                .when()
                .post("/auth/logout/")
                .then()
                .spec(logoutWithBlankTokenResponseSpec).extract()
                .as(LogoutWithoutOrBlankTokenResponseModel.class);

        String tokenActualDetailError = registrationResponse.refresh().get(0);

        assertThat(tokenActualDetailError).isEqualTo(BaseTestData.expectedRequiredError);
    }

}