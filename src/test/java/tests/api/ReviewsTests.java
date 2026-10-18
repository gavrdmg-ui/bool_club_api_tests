package tests.api;

import io.restassured.response.Response;
import models.clubs.ClubModel;
import models.clubs.CreateClubBodyModel;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.reviews.CreateReviewBodyModel;
import models.reviews.ReviewsModel;
import models.reviews.ReviewsResults;
import org.assertj.core.api.AbstractStringAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testdata.BaseTestData;
import testdata.ClubsTestData;
import testdata.ReviewsTestData;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.BaseSpec.noPermissionResponseSpec;
import static specs.BaseSpec.notFoundResponseSpec;
import static testdata.BaseTestData.dateRegex;
import static testdata.BaseTestData.noPermissionError;


public class ReviewsTests extends TestBase {

    public ReviewsTestData reviewsTestData;
    public ClubsTestData clubTestData;

    @BeforeEach
    public void prepareTestData() {
        reviewsTestData = new ReviewsTestData();
        clubTestData = new ClubsTestData();
    }

    @Test
    public void createReviewForClub() {
        LoginBodyModel loginData = new LoginBodyModel(BaseTestData.username, BaseTestData.password);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);

        String actualAccess = loginResponse.access();

        CreateClubBodyModel createData = new CreateClubBodyModel(
                clubTestData.bookTitle,
                clubTestData.bookAuthors,
                clubTestData.publicationYear,
                clubTestData.description,
                clubTestData.telegramChatLink
        );
        ClubModel clubResponse = api.clubs.createClub(createData, actualAccess);

        CreateReviewBodyModel requestBody = new CreateReviewBodyModel(clubResponse.id(),
                reviewsTestData.reviewData,
                reviewsTestData.assessment,
                reviewsTestData.readPages);

        ReviewsResults reviewResponse = api.review.postReview(requestBody, actualAccess);
        step("Проверяем что поля отзыва заполнены корректно", () -> {
            assertThat(reviewResponse.id()).isNotNull();
            assertThat(reviewResponse.club()).isEqualTo(clubResponse.id());
            assertThat(reviewResponse.user().username()).isEqualTo(BaseTestData.username);
            assertThat(reviewResponse.review()).isEqualTo(reviewsTestData.newReviewData);
            assertThat(reviewResponse.assessment()).isEqualTo(reviewsTestData.newAssessment);
            assertThat(reviewResponse.readPages()).isEqualTo(reviewsTestData.newReadPages);
            AbstractStringAssert<?> o = assertThat(reviewResponse.created()).containsPattern(dateRegex);
            assertThat(reviewResponse.modified()).isNull();
        });
    }

    @Test
    public void getReviewsForClubById() {
        ReviewsModel response = api.review.getReviews(reviewsTestData.clubWithReviewsId);

        step("Проверяем что все review принадлежат одному club", () -> {
            for (ReviewsResults res : response.results()) {
                assertThat(res.club()).isEqualTo(reviewsTestData.clubWithReviewsId);
            }
        });
    }

    @Test
    public void getFirstPageOfReviews() {
        ReviewsModel response = api.review.getReviews();

        step("Проверка количества ревью на странице", () -> {
            assertThat(response.count()).isEqualTo(reviewsTestData.baseReviewsPaginationSize);
        });

        step("Проверка наличия пагинации на странице", () -> {
            assertThat(response.next()).startsWith(reviewsTestData.paginationUrl);
            assertThat(response.previous()).isNull();

        });

        step("Проверяем обязательные поля для для каждого review в results ", () -> {
            for (ReviewsResults res : response.results()) {
                assertThat(res.id()).isNotNull().isPositive();
                assertThat(res.club()).isNotNull().isPositive();
                assertThat(res.user().id()).isNotNull().isPositive();
                assertThat(res.user().username()).isNotNull();
                assertThat(res.review()).isNotNull();
                assertThat(res.assessment()).isNotNull().isPositive();
                assertThat(res.readPages()).isNotNull();
                assertThat(res.created()).containsPattern(dateRegex);
                if (res.modified() != null) {
                    assertThat(res.modified()).containsPattern(dateRegex);
                }
            }
        });
    }

    @Test
    public void updateYourOwnReviewForClub() {
        LoginBodyModel loginData = new LoginBodyModel(BaseTestData.username, BaseTestData.password);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);

        String actualAccess = loginResponse.access();

        CreateClubBodyModel createData = new CreateClubBodyModel(
                clubTestData.bookTitle,
                clubTestData.bookAuthors,
                clubTestData.publicationYear,
                clubTestData.description,
                clubTestData.telegramChatLink
        );
        ClubModel clubResponse = api.clubs.createClub(createData, actualAccess);

        CreateReviewBodyModel createRequestBody = new CreateReviewBodyModel(clubResponse.id(),
                reviewsTestData.reviewData,
                reviewsTestData.assessment,
                reviewsTestData.readPages);

        ReviewsResults createReviewResponse = api.review.postReview(createRequestBody, actualAccess);

        CreateReviewBodyModel updateRequestBody = new CreateReviewBodyModel(clubResponse.id(),
                reviewsTestData.newReviewData,
                reviewsTestData.newAssessment,
                reviewsTestData.newReadPages);

        ReviewsResults updateReviewResponse = api.review.patchReview(createReviewResponse.id(), updateRequestBody, actualAccess);

        step("Проверяем что поля отзыва обновлены", () -> {
            assertThat(updateReviewResponse.id()).isNotNull();
            assertThat(updateReviewResponse.club()).isEqualTo(clubResponse.id());
            assertThat(updateReviewResponse.user().username()).isEqualTo(BaseTestData.username);
            assertThat(updateReviewResponse.review()).isEqualTo(updateRequestBody.review());
            assertThat(updateReviewResponse.assessment()).isEqualTo(updateRequestBody.assessment());
            assertThat(updateReviewResponse.readPages()).isEqualTo(updateRequestBody.readPages());
            assertThat(updateReviewResponse.created()).containsPattern(dateRegex);
            assertThat(updateReviewResponse.modified()).containsPattern(dateRegex);
        });
    }

    @Test
    public void updateAnotherUserReviewForClub() {
        LoginBodyModel loginData = new LoginBodyModel(BaseTestData.username, BaseTestData.password);

        SuccessfulLoginResponseModel loginResponseUserOne = api.auth.login(loginData);

        String actualAccessUserOne = loginResponseUserOne.access();

        CreateClubBodyModel createData = new CreateClubBodyModel(
                clubTestData.bookTitle,
                clubTestData.bookAuthors,
                clubTestData.publicationYear,
                clubTestData.description,
                clubTestData.telegramChatLink
        );
        ClubModel clubResponse = api.clubs.createClub(createData, actualAccessUserOne);

        CreateReviewBodyModel createRequestBody = new CreateReviewBodyModel(clubResponse.id(),
                reviewsTestData.reviewData,
                reviewsTestData.assessment,
                reviewsTestData.readPages);

        ReviewsResults createReviewResponse = api.review.postReview(createRequestBody, actualAccessUserOne);


        CreateReviewBodyModel updateRequestBody = new CreateReviewBodyModel(clubResponse.id(),
                reviewsTestData.newReviewData,
                reviewsTestData.newAssessment,
                reviewsTestData.newReadPages);

        LoginBodyModel loginDataUser2 = new LoginBodyModel(reviewsTestData.otherUserUserName, reviewsTestData.otherUserUserPassword);

        SuccessfulLoginResponseModel loginResponseUserTwo = api.auth.login(loginDataUser2);

        String actualAccessUserTwo = loginResponseUserTwo.access();

        Response updateReviewResponse = api.review.patchReview(createReviewResponse.id(), updateRequestBody, actualAccessUserTwo, noPermissionResponseSpec);

        step("Проверяем что поля отзыва обновлены", () -> {
            assertThat(updateReviewResponse.path("detail").toString()).isEqualTo(noPermissionError);
        });
    }

    @Test
    public void deleteYourOwnReview() {
        LoginBodyModel loginData = new LoginBodyModel(BaseTestData.username, BaseTestData.password);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);

        String actualAccess = loginResponse.access();

        CreateClubBodyModel createData = new CreateClubBodyModel(
                clubTestData.bookTitle,
                clubTestData.bookAuthors,
                clubTestData.publicationYear,
                clubTestData.description,
                clubTestData.telegramChatLink
        );
        ClubModel clubResponse = api.clubs.createClub(createData, actualAccess);

        CreateReviewBodyModel requestBody = new CreateReviewBodyModel(clubResponse.id(),
                reviewsTestData.reviewData,
                reviewsTestData.assessment,
                reviewsTestData.readPages);

        ReviewsResults reviewResponse = api.review.postReview(requestBody, actualAccess);

        api.review.deleteReview(reviewResponse.id(), actualAccess);

        Response getResponse = api.review.getReview(reviewResponse.id(), notFoundResponseSpec);

        step("Проверяем что отзыва не существует", () -> {
            assertThat(getResponse.path("detail").toString()).isEqualTo(reviewsTestData.notFoundReviewError);
        });
    }

    @Test
    public void deleteAnotherUserReviewForClub() {
        LoginBodyModel loginDataUserOne = new LoginBodyModel(BaseTestData.username, BaseTestData.password);

        SuccessfulLoginResponseModel loginResponseUserOne = api.auth.login(loginDataUserOne);

        String actualAccessUserOne = loginResponseUserOne.access();

        CreateClubBodyModel createData = new CreateClubBodyModel(
                clubTestData.bookTitle,
                clubTestData.bookAuthors,
                clubTestData.publicationYear,
                clubTestData.description,
                clubTestData.telegramChatLink
        );
        ClubModel clubResponse = api.clubs.createClub(createData, actualAccessUserOne);

        CreateReviewBodyModel requestBody = new CreateReviewBodyModel(clubResponse.id(),
                reviewsTestData.reviewData,
                reviewsTestData.assessment,
                reviewsTestData.readPages);

        ReviewsResults reviewResponse = api.review.postReview(requestBody, actualAccessUserOne);

        LoginBodyModel loginDataUser2 = new LoginBodyModel(reviewsTestData.otherUserUserName, reviewsTestData.otherUserUserPassword);

        SuccessfulLoginResponseModel loginResponseUserTwo = api.auth.login(loginDataUser2);

        String actualAccessUserTwo = loginResponseUserTwo.access();

        Response deleteResponse = api.review.deleteReview(reviewResponse.id(), actualAccessUserTwo, noPermissionResponseSpec);

        step("Проверяем удаление чужого отзыва запрещено", () -> {
            assertThat(deleteResponse.path("detail").toString()).isEqualTo(noPermissionError);
        });

    }

}
