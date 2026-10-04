package api;

import io.qameta.allure.Step;
import models.login.LoginBodyModel;
import models.login.LoginWithoutOrBlankCredentialsResponseModel;
import models.login.SuccessfulLoginResponseModel;
import models.login.WrongCredentialsLoginResponseModel;
import models.logout.LogoutBodyModel;
import models.logout.LogoutWithoutOrBlankTokenResponseModel;

import static io.restassured.RestAssured.given;
import static specs.login.LoginSpec.*;
import static specs.logout.LogoutSpec.*;

public class AuthApiClient {
    @Step("Авторизация и получение токена")
    public SuccessfulLoginResponseModel login(LoginBodyModel loginData) {
        return given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec).extract().as(SuccessfulLoginResponseModel.class);
    }

    @Step("Авторизация с неверными данными")
    public WrongCredentialsLoginResponseModel loginWithWrongCredentials(LoginBodyModel loginData) {
        return given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(wrongCredentialsLoginResponseSpec).extract().as(WrongCredentialsLoginResponseModel.class);
    }

    @Step("Авторизация с пустыми полями")
    public LoginWithoutOrBlankCredentialsResponseModel loginWithoutOrBlankCredentials(LoginBodyModel loginData) {
        return given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(loginWithoutCredentialsResponseSpec).extract().as(LoginWithoutOrBlankCredentialsResponseModel.class);
    }

    @Step("Авторизация с пустым body")
    public LoginWithoutOrBlankCredentialsResponseModel loginWithoutOrBlankCredentials() {
        return given(loginRequestSpec)
                .body("{}")
                .when()
                .post("/auth/token/")
                .then()
                .spec(loginWithoutCredentialsResponseSpec).extract().as(LoginWithoutOrBlankCredentialsResponseModel.class);
    }

    @Step("Отправка запроса logout")
    public void logout(LogoutBodyModel logoutData) {
        given(logoutRequestSpec)
                .body(logoutData)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(successfulLogoutResponseSpec);
    }

    @Step("Отправка запроса logout c пустым токеном")
    public LogoutWithoutOrBlankTokenResponseModel logoutWithoutOrBlankToken(LogoutBodyModel logoutData) {
        return given()
                .spec(logoutRequestSpec)
                .body(logoutData)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(logoutWithBlankTokenResponseSpec).extract()
                .as(LogoutWithoutOrBlankTokenResponseModel.class);
    }

    @Step("Отправка запроса logout с пустым body")
    public LogoutWithoutOrBlankTokenResponseModel logoutWithoutOrBlankToken() {
        return given()
                .spec(logoutRequestSpec)
                .body("{}")
                .when()
                .post("/auth/logout/")
                .then()
                .spec(logoutWithBlankTokenResponseSpec).extract()
                .as(LogoutWithoutOrBlankTokenResponseModel.class);
    }

}
