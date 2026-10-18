package tests.api;

import models.login.LoginBodyModel;
import models.login.LoginWithoutOrBlankCredentialsResponseModel;
import models.login.SuccessfulLoginResponseModel;
import models.login.WrongCredentialsLoginResponseModel;
import org.junit.jupiter.api.Test;
import testdata.BaseTestData;
import testdata.LoginTestData;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

public class LoginTests extends TestBase {


    @Test
    public void successfulLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel(BaseTestData.username, BaseTestData.password);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);

        step("Проверка наличия токенов в теле ответа", () -> {
            String actualAccess = loginResponse.access();
            String actualRefresh = loginResponse.refresh();

            assertThat(actualAccess).startsWith(LoginTestData.expectedTokenPath);
            assertThat(actualRefresh).startsWith(LoginTestData.expectedTokenPath);
            assertThat(actualAccess).isNotEqualTo(actualRefresh);
        });
    }

    @Test
    public void wrongCredentialsLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel(BaseTestData.username, LoginTestData.wrongPassword);

        WrongCredentialsLoginResponseModel loginResponse = api.auth.loginWithWrongCredentials(loginData);

        step("Проверка текста ошибки в теле ответа", () -> {
            String actualDetailError = loginResponse.detail();

            assertThat(actualDetailError).isEqualTo(LoginTestData.expectedDetailErrorWrongCredentials);
        });
    }

    @Test
    public void loginWithoutCredentialsTest() {
        LoginWithoutOrBlankCredentialsResponseModel loginResponse = api.auth.loginWithoutOrBlankCredentials();

        step("Проверка текста ошибки в теле ответа", () -> {
            String usernameActualDetailError = loginResponse.username().get(0);
            String passwordActualDetailError = loginResponse.password().get(0);

            assertThat(usernameActualDetailError).isEqualTo(BaseTestData.expectedRequiredError);
            assertThat(passwordActualDetailError).isEqualTo(BaseTestData.expectedRequiredError);
        });
    }

    @Test
    public void loginWithBlankCredentialsTest() {
        LoginBodyModel loginData = new LoginBodyModel("", "");

        LoginWithoutOrBlankCredentialsResponseModel loginResponse = api.auth.loginWithoutOrBlankCredentials(loginData);

        step("Проверка текста ошибки в теле ответа", () -> {
            String usernameActualDetailError = loginResponse.username().get(0);
            String passwordActualDetailError = loginResponse.password().get(0);

            assertThat(usernameActualDetailError).isEqualTo(BaseTestData.expectedBlankError);
            assertThat(passwordActualDetailError).isEqualTo(BaseTestData.expectedBlankError);
        });
    }

}