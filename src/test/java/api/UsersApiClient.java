package api;

import io.qameta.allure.Step;
import models.registration.ExistingUserResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.RegistrationWithoutOrBlankFieldsResponseModel;
import models.registration.SuccessfulRegistrationResponseModel;
import models.update.SuccessfulUpdateResponseModel;
import models.update.UpdateBodyModel;
import models.update.UpdateWithoutAuthTokenResponseModel;

import static io.restassured.RestAssured.given;
import static specs.registration.RegistrationSpec.*;
import static specs.update.UpdateSpec.*;

public class UsersApiClient {
    @Step("Регистрация пользователя")
    public SuccessfulRegistrationResponseModel register(RegistrationBodyModel registrationData){
        return given()
                .spec(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec)
                .extract()
                .as(SuccessfulRegistrationResponseModel.class);
    }
    @Step("Повторная регистрация пользователя")
    public ExistingUserResponseModel registerExistUser(RegistrationBodyModel registrationData){
        return given()
                .spec(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(existingUserRegistrationResponseSpec)
                .extract()
                .as(ExistingUserResponseModel.class);

    }
    @Step("Регистрация пользователя с пустыми полям")
    public RegistrationWithoutOrBlankFieldsResponseModel registerWithBlankFields(RegistrationBodyModel registrationData){
        return given()
                .spec(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(blankFieldsRegistrationResponseSpec)
                .extract()
                .as(RegistrationWithoutOrBlankFieldsResponseModel.class);
    }
    @Step("Регистрация пользователя с пустым телом запроса")
    public RegistrationWithoutOrBlankFieldsResponseModel registerWithBlankFields(){
        return given()
                .spec(registrationRequestSpec)
                .body("{}")
                .when()
                .post("/users/register/")
                .then()
                .spec(blankFieldsRegistrationResponseSpec)
                .extract()
                .as(RegistrationWithoutOrBlankFieldsResponseModel.class);
    }
    @Step("Обновление данных пользовтеля")
    public SuccessfulUpdateResponseModel update (UpdateBodyModel updateData,String actualAccess){
        return given(updateRequestSpec)
                .auth()
                .oauth2(actualAccess)
                .body(updateData)
                .when()
                .put("/users/me/")
                .then()
                .spec(successfulUpdateResponseSpec).extract().as(SuccessfulUpdateResponseModel.class);
    }
    public UpdateWithoutAuthTokenResponseModel updateWithoutAutToken(UpdateBodyModel updateData){
        return given(updateRequestSpec)
                .body(updateData)
                .when()
                .put("/users/me/")
                .then()
                .spec(updateWithoutAuthTokenResponseSpec).extract().as(UpdateWithoutAuthTokenResponseModel.class);
    }
}
