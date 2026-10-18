package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import models.reviews.CreateReviewBodyModel;
import models.reviews.ReviewsModel;
import models.reviews.ReviewsResults;

import static api.ApiClient.successfulDeleteResponseSpec;
import static io.restassured.RestAssured.given;
import static specs.clubs.ClubsSpec.clubsRequestSpec;
import static specs.reviews.ReviewsSpec.*;

public class ReviewsApiClient {
    @Step("Получение ревью по конкретному клубу")
    public ReviewsModel getReviews(Integer clubId) {
        return given(clubsRequestSpec)
                .when()
                .queryParam("club", clubId)
                .get("/clubs/reviews/")
                .then()
                .spec(successfulReviewsListResponseSpec)
                .extract()
                .as(ReviewsModel.class);
    }

    @Step("Получение всех ревью")
    public ReviewsModel getReviews() {
        return given(clubsRequestSpec)
                .when()
                .get("/clubs/reviews/")
                .then()
                .spec(successfulReviewsListResponseSpec)
                .extract()
                .as(ReviewsModel.class);
    }

    @Step("Получение ревью по id")
    public Response getReview(Integer reviewId, ResponseSpecification spec) {
        return given(clubsRequestSpec)
                .when()
                .get("/clubs/reviews/{id}/", reviewId)
                .then()
                .spec(spec)
                .extract()
                .response();
    }

    public Response getReviewById() {
        return null;
    }

    @Step("Создание отзыва клуба")
    public ReviewsResults postReview(CreateReviewBodyModel requestBody, String actualAccess) {
        return given(clubsRequestSpec)
                .when()
                .auth()
                .oauth2(actualAccess)
                .body(requestBody)
                .post("/clubs/reviews/")
                .then()
                .spec(successfulCreateReviewResponseSpec)
                .extract()
                .as(ReviewsResults.class);
    }

    public ReviewsResults patchReview(Integer reviewId, CreateReviewBodyModel requestBody, String actualAccess) {
        return given(clubsRequestSpec)
                .when()
                .auth()
                .oauth2(actualAccess)
                .body(requestBody)
                .patch("/clubs/reviews/{id}/", reviewId)
                .then()
                .spec(successfulUpdateReviewResponseSpec)
                .extract()
                .as(ReviewsResults.class);
    }

    public Response patchReview(Integer reviewId, CreateReviewBodyModel requestBody, String actualAccess, ResponseSpecification spec) {
        return given(clubsRequestSpec)
                .when()
                .auth()
                .oauth2(actualAccess)
                .body(requestBody)
                .patch("/clubs/reviews/{id}/", reviewId)
                .then()
                .spec(spec)
                .extract()
                .response();
    }

    @Step("Удаление клуба")
    public void deleteReview(int reviewId, String actualAccess) {
        given(clubsRequestSpec)
                .auth()
                .oauth2(actualAccess)
                .when()
                .delete("/clubs/reviews/{id}/", reviewId)
                .then()
                .spec(successfulDeleteResponseSpec);
    }

    @Step("Удаление клуба")
    public Response deleteReview(int reviewId, String actualAccess, ResponseSpecification spec) {
        return given(clubsRequestSpec)
                .auth()
                .oauth2(actualAccess)
                .when()
                .delete("/clubs/reviews/{id}/", reviewId)
                .then()
                .spec(spec)
                .extract()
                .response();
    }

}
