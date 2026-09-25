package tests;

import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.update.SuccessfulUpdateResponseModel;
import models.update.UpdateBodyModel;
import models.update.UpdateWithoutAuthTokenResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testdata.BaseTestData;
import testdata.UpdateTestData;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.login.LoginSpec.loginRequestSpec;
import static specs.login.LoginSpec.successfulLoginResponseSpec;
import static specs.update.UpdateSpec.*;

public class UpdateUserTests extends TestBase {

    public UpdateTestData testData;

    @BeforeEach
    public void prepareTestData() {
        testData = new UpdateTestData();
    }

    @Test
    public void successfulUpdateAllFieldsTest() {

        LoginBodyModel loginData = new LoginBodyModel(BaseTestData.username, BaseTestData.password);

        SuccessfulLoginResponseModel loginResponse = step("Авторизация под пользователем для получения токена", () -> given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec).extract().as(SuccessfulLoginResponseModel.class));

        String actualAccess = loginResponse.access();
        UpdateBodyModel updateData = new UpdateBodyModel(BaseTestData.username, testData.firstName, testData.lastName, testData.email);

        SuccessfulUpdateResponseModel updateResponse = step("Обновление информации о пользователе и проверка кода 200", () -> given(updateRequestSpec)
                .auth()
                .oauth2(actualAccess)
                .body(updateData)
                .when()
                .put("/users/me/")
                .then()
                .spec(successfulUpdateResponseSpec).extract().as(SuccessfulUpdateResponseModel.class));

        assertThat(updateResponse.id()).isGreaterThan(0);
        assertThat(updateResponse.username()).isEqualTo(BaseTestData.username);
        assertThat(updateResponse.firstName()).isEqualTo(testData.firstName);
        assertThat(updateResponse.lastName()).isEqualTo(testData.lastName);
        assertThat(updateResponse.email()).isEqualTo(testData.email);
        assertThat(updateResponse.remoteAddr()).matches(BaseTestData.ipAddrRegexp);
    }

    @Test
    public void updateWithoutAuthToken() {

        UpdateBodyModel updateData = new UpdateBodyModel(BaseTestData.username, testData.firstName, testData.lastName, testData.email);

        UpdateWithoutAuthTokenResponseModel updateResponse = step("Обновление информации о пользователе без токена автризации и проверка кода 401", () -> given(updateRequestSpec)
                .body(updateData)
                .when()
                .put("/users/me/")
                .then()
                .spec(updateWithoutAuthTokenResponseSpec).extract().as(UpdateWithoutAuthTokenResponseModel.class));

        assertThat(updateResponse.detail()).isEqualTo(UpdateTestData.expectedDetailErrorWithoutAuthToken);
    }
}
