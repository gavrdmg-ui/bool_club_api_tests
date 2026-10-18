package tests.api;

import models.login.LoginBodyModel;
import models.logout.LogoutBodyModel;
import models.logout.LogoutWithoutOrBlankTokenResponseModel;
import org.junit.jupiter.api.Test;
import testdata.BaseTestData;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

public class LogoutTests extends TestBase {

    @Test
    public void successfulLogoutTest() {
        LoginBodyModel loginData = new LoginBodyModel(BaseTestData.username, BaseTestData.password);

        String refreshToken = api.auth.login(loginData).refresh();

        api.auth.logout(new LogoutBodyModel(refreshToken));
    }

    @Test
    public void logoutWithBlankToken() {
        LogoutBodyModel logoutData = new LogoutBodyModel("");

        LogoutWithoutOrBlankTokenResponseModel registrationResponse = api.auth.logoutWithoutOrBlankToken(logoutData);

        step("Проверка текста ошибки в теле ответа", () -> {
            String tokenActualDetailError = registrationResponse.refresh().get(0);

            assertThat(tokenActualDetailError).isEqualTo(BaseTestData.expectedBlankError);
        });
    }

    @Test
    public void logoutWithoutToken() {

        LogoutWithoutOrBlankTokenResponseModel registrationResponse = api.auth.logoutWithoutOrBlankToken();

        step("Проверка текста ошибки в теле ответа", () -> {
            String tokenActualDetailError = registrationResponse.refresh().get(0);

            assertThat(tokenActualDetailError).isEqualTo(BaseTestData.expectedRequiredError);
        });
    }

}