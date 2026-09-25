package tests;

import models.registration.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testdata.BaseTestData;
import testdata.RegistrationTestData;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.registration.RegistrationSpec.*;


public class RegistrationTests extends TestBase {

    public RegistrationTestData testData;

    @BeforeEach
    public void prepareTestData() {
        testData = new RegistrationTestData();
    }

    @Test
    public void successfulRegistrationTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.username, testData.password);

        SuccessfulRegistrationResponseModel registrationResponse = step("Регистрация нового пользвотеля и проверка ответа 201", () -> given()
                .spec(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec)
                .extract()
                .as(SuccessfulRegistrationResponseModel.class));

        assertThat(registrationResponse.id()).isGreaterThan(0);
        assertThat(registrationResponse.username()).isEqualTo(testData.username);
        assertThat(registrationResponse.firstName()).isEqualTo("");
        assertThat(registrationResponse.lastName()).isEqualTo("");
        assertThat(registrationResponse.email()).isEqualTo("");


        assertThat(registrationResponse.remoteAddr()).matches(BaseTestData.ipAddrRegexp);
    }

    @Test
    public void existingUserWrongRegistrationTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.username, testData.password);

        SuccessfulRegistrationResponseModel firstRegistrationResponse = step("Регистрация нового пользвотеля и проверка ответа 201", () -> given()
                .spec(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec)
                .extract()
                .as(SuccessfulRegistrationResponseModel.class));

        assertThat(firstRegistrationResponse.username()).isEqualTo(testData.username);

        ExistingUserResponseModel secondRegistrationResponse = step("Регистрация пользвотеля повторно и проверка ответа 400", () -> given()
                .spec(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(existingUserRegistrationResponseSpec)
                .extract()
                .as(ExistingUserResponseModel.class));


        String actualError = secondRegistrationResponse.username().get(0);
        assertThat(actualError).isEqualTo(RegistrationTestData.expectedExistUserError);
    }

    @Test
    public void registrationWithBlankFieldsTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel("", "");

        RegistrationWithoutOrBlankFieldsResponseModel secondRegistrationResponse = step("Регистрация пользвотеля с пустыми полями в теле запроса и проверка ответа 400", () -> given()
                .spec(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(blankFieldsRegistrationResponseSpec)
                .extract()
                .as(RegistrationWithoutOrBlankFieldsResponseModel.class));

        String usernameActualDetailError = secondRegistrationResponse.username().get(0);
        String passwordActualDetailError = secondRegistrationResponse.username().get(0);

        assertThat(usernameActualDetailError).isEqualTo(BaseTestData.expectedBlankError);
        assertThat(passwordActualDetailError).isEqualTo(BaseTestData.expectedBlankError);

    }

    @Test
    public void registrationWithoutFieldsTest() {

        RegistrationWithoutOrBlankFieldsResponseModel secondRegistrationResponse = step("Регистрация пользвотеля с пустым телом запроса и проверка ответа 400", () -> given()
                .spec(registrationRequestSpec)
                .body("{}")
                .when()
                .post("/users/register/")
                .then()
                .spec(blankFieldsRegistrationResponseSpec)
                .extract()
                .as(RegistrationWithoutOrBlankFieldsResponseModel.class));

        String usernameActualDetailError = secondRegistrationResponse.username().get(0);
        String passwordActualDetailError = secondRegistrationResponse.username().get(0);

        assertThat(usernameActualDetailError).isEqualTo(BaseTestData.expectedRequiredError);
        assertThat(passwordActualDetailError).isEqualTo(BaseTestData.expectedRequiredError);

    }

}