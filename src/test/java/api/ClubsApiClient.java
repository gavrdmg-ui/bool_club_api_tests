package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import models.WithoutAuthTokenResponseModel;
import models.clubs.ClubModel;
import models.clubs.ClubsListResponseModel;
import models.clubs.CreateClubBodyModel;

import static api.ApiClient.successfulDeleteResponseSpec;
import static io.restassured.RestAssured.given;
import static specs.BaseSpec.withoutAuthTokenResponseSpec;
import static specs.clubs.ClubsSpec.*;

public class ClubsApiClient {
    @Step("Получение списка клубов")
    public ClubsListResponseModel getClubs() {
        return given(clubsRequestSpec)
                .when()
                .get("/clubs/")
                .then()
                .spec(successfulClubsListResponseSpec)
                .extract()
                .as(ClubsListResponseModel.class);
    }

    @Step("Получение клуба по Id")
    public Response getClubsById(int clubId, ResponseSpecification spec) {
        return given(clubsRequestSpec)
                .when()
                .get("/clubs/{id}/", clubId)
                .then()
                .spec(spec)
                .extract()
                .response();
    }


    @Step("Создание нового клуба")
    public ClubModel createClub(CreateClubBodyModel requestBody, String actualAccess) {
        return given(clubsRequestSpec)
                .auth()
                .oauth2(actualAccess)
                .body(requestBody)
                .when()
                .post("/clubs/")
                .then()
                .spec(successfulCreateClubResponseSpec)
                .extract()
                .as(ClubModel.class);
    }

    @Step("Создание нового клуба без авторизации")
    public WithoutAuthTokenResponseModel createClub(CreateClubBodyModel requestBody) {
        return given(clubsRequestSpec)
                .body(requestBody)
                .when()
                .post("/clubs/")
                .then()
                .spec(withoutAuthTokenResponseSpec)
                .extract()
                .as(WithoutAuthTokenResponseModel.class);
    }

    @Step("Обновление клуба")
    public ClubModel updateclub(CreateClubBodyModel requestBody, String actualAccess, int clubId) {
        return given(clubsRequestSpec)
                .auth()
                .oauth2(actualAccess)
                .body(requestBody)
                .when()
                .put("/clubs/{id}/", clubId)
                .then()
                .spec(successfulClubResponseSpec)
                .extract()
                .as(ClubModel.class);
    }

    @Step("Удаление клуба")
    public void deleteClub(int clubId, String actualAccess) {
        given(clubsRequestSpec)
                .auth()
                .oauth2(actualAccess)
                .when()
                .delete("/clubs/{id}/", clubId)
                .then()
                .spec(successfulDeleteResponseSpec);
    }
}
