package tests;

import models.login.LoginBodyModel;
import models.login.LoginWithoutOrBlankCredentialsResponseModel;
import models.login.SuccessfulLoginResponseModel;
import models.login.WrongCredentialsLoginResponseModel;
import org.junit.jupiter.api.Test;
import testdata.BaseTestData;
import testdata.LoginTestData;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.login.LoginSpec.*;

public class LoginTests extends TestBase {


    @Test
    public void successfulLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel(BaseTestData.username, BaseTestData.password);

        SuccessfulLoginResponseModel loginResponse = step("Отправка запроса login для существующего пользователя и проверка ответа 200", () ->
                given(loginRequestSpec).body(loginData).when().post("/auth/token/").then().spec(successfulLoginResponseSpec).extract().as(SuccessfulLoginResponseModel.class));


        String actualAccess = loginResponse.access();
        String actualRefresh = loginResponse.refresh();

        assertThat(actualAccess).startsWith(LoginTestData.expectedTokenPath);
        assertThat(actualRefresh).startsWith(LoginTestData.expectedTokenPath);
        assertThat(actualAccess).isNotEqualTo(actualRefresh);
    }

    @Test
    public void wrongCredentialsLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel(BaseTestData.username, LoginTestData.wrongPassword);

        WrongCredentialsLoginResponseModel loginResponse = step("Отправка запроса login с неправильным паролем и проверка ответа 401", () ->
                given(loginRequestSpec)
                        .body(loginData)
                        .when()
                        .post("/auth/token/")
                        .then()
                        .spec(wrongCredentialsLoginResponseSpec).extract().as(WrongCredentialsLoginResponseModel.class));

        String actualDetailError = loginResponse.detail();

        assertThat(actualDetailError).isEqualTo(LoginTestData.expectedDetailErrorWrongCredentials);
    }

    @Test
    public void loginWithoutCredentialsTest() {
        LoginWithoutOrBlankCredentialsResponseModel loginResponse = step("Отправка запроса login с пустым телом проверка ответа 400", () -> given(loginRequestSpec)
                .body("{}")
                .when()
                .post("/auth/token/")
                .then()
                .spec(loginWithoutCredentialsResponseSpec).extract().as(LoginWithoutOrBlankCredentialsResponseModel.class));

        String usernameActualDetailError = loginResponse.username().get(0);
        String passwordActualDetailError = loginResponse.password().get(0);

        assertThat(usernameActualDetailError).isEqualTo(BaseTestData.expectedRequiredError);
        assertThat(passwordActualDetailError).isEqualTo(BaseTestData.expectedRequiredError);
    }

    @Test
    public void loginWithBlankCredentialsTest() {
        LoginBodyModel loginData = new LoginBodyModel("", "");

        LoginWithoutOrBlankCredentialsResponseModel loginResponse = step("Отправка запроса login с пустыми поля в теле запроса проверка ответа 400", () -> given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(loginWithoutCredentialsResponseSpec).extract().as(LoginWithoutOrBlankCredentialsResponseModel.class));

        String usernameActualDetailError = loginResponse.username().get(0);
        String passwordActualDetailError = loginResponse.password().get(0);

        assertThat(usernameActualDetailError).isEqualTo(BaseTestData.expectedBlankError);
        assertThat(passwordActualDetailError).isEqualTo(BaseTestData.expectedBlankError);
    }

}