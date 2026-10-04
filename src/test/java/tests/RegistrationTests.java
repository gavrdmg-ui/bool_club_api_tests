package tests;

import models.registration.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testdata.BaseTestData;
import testdata.RegistrationTestData;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

public class RegistrationTests extends TestBase {

    public RegistrationTestData testData;

    @BeforeEach
    public void prepareTestData() {
        testData = new RegistrationTestData();
    }

    @Test
    public void successfulRegistrationTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.username, testData.password);

        SuccessfulRegistrationResponseModel registrationResponse = api.users.register(registrationData);

        step("Проверка данных пользователя после успешной регистарции", () -> {
            assertThat(registrationResponse.id()).isGreaterThan(0);
            assertThat(registrationResponse.username()).isEqualTo(testData.username);
            assertThat(registrationResponse.firstName()).isEqualTo("");
            assertThat(registrationResponse.lastName()).isEqualTo("");
            assertThat(registrationResponse.email()).isEqualTo("");
            assertThat(registrationResponse.remoteAddr()).matches(BaseTestData.ipAddrRegexp);
        });
    }

    @Test
    public void existingUserWrongRegistrationTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.username, testData.password);

        SuccessfulRegistrationResponseModel firstRegistrationResponse = api.users.register(registrationData);

        assertThat(firstRegistrationResponse.username()).isEqualTo(testData.username);

        ExistingUserResponseModel secondRegistrationResponse = api.users.registerExistUser(registrationData);

        step("Проверка текста ошибки в теле ответа", () -> {
            String actualError = secondRegistrationResponse.username().get(0);
            assertThat(actualError).isEqualTo(RegistrationTestData.expectedExistUserError);
        });
    }

    @Test
    public void registrationWithBlankFieldsTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel("", "");

        RegistrationWithoutOrBlankFieldsResponseModel secondRegistrationResponse = api.users.registerWithBlankFields(registrationData);

        step("Проверка текста ошибки в теле ответа", () -> {
            String usernameActualDetailError = secondRegistrationResponse.username().get(0);
            String passwordActualDetailError = secondRegistrationResponse.username().get(0);

            assertThat(usernameActualDetailError).isEqualTo(BaseTestData.expectedBlankError);
            assertThat(passwordActualDetailError).isEqualTo(BaseTestData.expectedBlankError);
        });

    }

    @Test
    public void registrationWithoutFieldsTest() {

        RegistrationWithoutOrBlankFieldsResponseModel secondRegistrationResponse = api.users.registerWithBlankFields();


        step("Проверка текста ошибки в теле ответа", () -> {
            String usernameActualDetailError = secondRegistrationResponse.username().get(0);
            String passwordActualDetailError = secondRegistrationResponse.username().get(0);

            assertThat(usernameActualDetailError).isEqualTo(BaseTestData.expectedRequiredError);
            assertThat(passwordActualDetailError).isEqualTo(BaseTestData.expectedRequiredError);
        });
    }

}