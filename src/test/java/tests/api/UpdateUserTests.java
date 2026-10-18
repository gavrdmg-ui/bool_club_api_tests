package tests.api;

import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.update.SuccessfulUpdateResponseModel;
import models.update.UpdateBodyModel;
import models.WithoutAuthTokenResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testdata.BaseTestData;
import testdata.UpdateTestData;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

public class UpdateUserTests extends TestBase {

    public UpdateTestData testData;

    @BeforeEach
    public void prepareTestData() {
        testData = new UpdateTestData();
    }

    @Test
    public void successfulUpdateAllFieldsTest() {

        LoginBodyModel loginData = new LoginBodyModel(BaseTestData.username, BaseTestData.password);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);

        String actualAccess = loginResponse.access();
        UpdateBodyModel updateData = new UpdateBodyModel(BaseTestData.username, testData.firstName, testData.lastName, testData.email);

        SuccessfulUpdateResponseModel updateResponse = api.users.update(updateData, actualAccess);

        step("Проверка корректности новых данных", () -> {
            assertThat(updateResponse.id()).isGreaterThan(0);
            assertThat(updateResponse.username()).isEqualTo(BaseTestData.username);
            assertThat(updateResponse.firstName()).isEqualTo(testData.firstName);
            assertThat(updateResponse.lastName()).isEqualTo(testData.lastName);
            assertThat(updateResponse.email()).isEqualTo(testData.email);
            assertThat(updateResponse.remoteAddr()).matches(BaseTestData.ipAddrRegexp);
        });
    }

    @Test
    public void updateWithoutAuthToken() {

        UpdateBodyModel updateData = new UpdateBodyModel(BaseTestData.username, testData.firstName, testData.lastName, testData.email);

        WithoutAuthTokenResponseModel updateResponse = api.users.updateWithoutAutToken(updateData);

        step("Проверка текста ошибки в теле ответа", () -> {
            assertThat(updateResponse.detail()).isEqualTo(BaseTestData.expectedDetailErrorWithoutAuthToken);
        });
    }
}
