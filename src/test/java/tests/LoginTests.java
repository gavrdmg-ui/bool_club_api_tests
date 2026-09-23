package tests;

import models.login.LoginBodyModel;
import models.login.LoginWithoutOrBlankCredentialsResponseModel;
import models.login.SuccessfulLoginResponseModel;
import models.login.WrongCredentialsLoginResponseModel;
import org.junit.jupiter.api.Test;
import testdata.BaseTestData;
import testdata.LoginTestData;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.login.LoginSpec.*;

public class LoginTests extends TestBase {


    @Test
    public void successfulLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel(BaseTestData.username, BaseTestData.password);

        SuccessfulLoginResponseModel loginResponse = given(loginRequestSpec).body(loginData).when().post("/auth/token/").then().spec(successfulLoginResponseSpec).extract().as(SuccessfulLoginResponseModel.class);


        String actualAccess = loginResponse.access();
        String actualRefresh = loginResponse.refresh();

        assertThat(actualAccess).startsWith(LoginTestData.expectedTokenPath);
        assertThat(actualRefresh).startsWith(LoginTestData.expectedTokenPath);
        assertThat(actualAccess).isNotEqualTo(actualRefresh);
    }

    @Test
    public void wrongCredentialsLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel(BaseTestData.username, LoginTestData.wrongPassword);

        WrongCredentialsLoginResponseModel loginResponse = given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(wrongCredentialsLoginResponseSpec).extract().as(WrongCredentialsLoginResponseModel.class);

        String actualDetailError = loginResponse.detail();

        assertThat(actualDetailError).isEqualTo(LoginTestData.expectedDetailErrorWrongCredentials);
    }

    @Test
    public void loginWithoutCredentialsTest() {
        LoginWithoutOrBlankCredentialsResponseModel loginResponse = given(loginRequestSpec)
                .body("{}")
                .when()
                .post("/auth/token/")
                .then()
                .spec(loginWithoutCredentialsResponseSpec).extract().as(LoginWithoutOrBlankCredentialsResponseModel.class);

        String usernameActualDetailError = loginResponse.username().get(0);
        String passwordActualDetailError = loginResponse.password().get(0);

        assertThat(usernameActualDetailError).isEqualTo(BaseTestData.expectedRequiredError);
        assertThat(passwordActualDetailError).isEqualTo(BaseTestData.expectedRequiredError);
    }
    @Test
    public void loginWithBlankCredentialsTest() {
        LoginBodyModel loginData = new LoginBodyModel("", "");

        LoginWithoutOrBlankCredentialsResponseModel loginResponse = given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(loginWithoutCredentialsResponseSpec).extract().as(LoginWithoutOrBlankCredentialsResponseModel.class);

        String usernameActualDetailError = loginResponse.username().get(0);
        String passwordActualDetailError = loginResponse.password().get(0);

        assertThat(usernameActualDetailError).isEqualTo(BaseTestData.expectedBlankError);
        assertThat(passwordActualDetailError).isEqualTo(BaseTestData.expectedBlankError);
    }

}