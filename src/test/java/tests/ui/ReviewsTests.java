package tests.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.clubs.ClubModel;
import models.clubs.CreateClubBodyModel;
import models.localstorage.AuthModel;
import models.localstorage.User;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.reviews.CreateReviewBodyModel;
import models.reviews.ReviewsResults;
import models.update.SuccessfulUpdateResponseModel;
import org.junit.jupiter.api.Test;
import testdata.BaseTestData;

import static io.qameta.allure.Allure.step;

public class ReviewsTests extends TestBase {

    @Test
    public void reviewDisplayInClub() {
        LoginBodyModel loginData = new LoginBodyModel(BaseTestData.username, BaseTestData.password);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);

        String actualAccess = loginResponse.access();
        String actualRefresh = loginResponse.access();

        SuccessfulUpdateResponseModel userInfoResponse = api.users.getUserInfo(actualAccess);


        CreateClubBodyModel createData = new CreateClubBodyModel(
                clubsTestData.bookTitle,
                clubsTestData.bookAuthors,
                clubsTestData.publicationYear,
                clubsTestData.description,
                clubsTestData.telegramChatLink
        );
        ClubModel clubResponse = api.clubs.createClub(createData, actualAccess);

        CreateReviewBodyModel requestBody = new CreateReviewBodyModel(clubResponse.id(),
                reviewTestData.reviewData,
                reviewTestData.assessment,
                reviewTestData.readPages);

        api.review.postReview(requestBody, actualAccess);

        String localStorageAuthBody = step("Создание JSON авторизации для localStorage", () -> {
            User user = new User(
                    userInfoResponse.id(),
                    userInfoResponse.username(),
                    userInfoResponse.firstName(),
                    userInfoResponse.lastName(),
                    userInfoResponse.email(),
                    userInfoResponse.remoteAddr()
            );
            AuthModel auth = new AuthModel(user, actualAccess, actualRefresh, true);
            return new ObjectMapper().writeValueAsString(auth);
        });

        reviewPage.openPageWithLocalStorage(localStorageAuthBody)
                .openClubPageById(clubResponse.id())
                .checkReview(reviewTestData.reviewData, BaseTestData.username, reviewTestData.assessment, reviewTestData.readPages);
    }

    @Test
    public void reviewNotDisplayedAfterDelete() {
        LoginBodyModel loginData = new LoginBodyModel(BaseTestData.username, BaseTestData.password);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);

        String actualAccess = loginResponse.access();
        String actualRefresh = loginResponse.access();

        SuccessfulUpdateResponseModel userInfoResponse = api.users.getUserInfo(actualAccess);


        CreateClubBodyModel createData = new CreateClubBodyModel(
                clubsTestData.bookTitle,
                clubsTestData.bookAuthors,
                clubsTestData.publicationYear,
                clubsTestData.description,
                clubsTestData.telegramChatLink
        );
        ClubModel clubResponse = api.clubs.createClub(createData, actualAccess);

        CreateReviewBodyModel requestBody = new CreateReviewBodyModel(clubResponse.id(),
                reviewTestData.reviewData,
                reviewTestData.assessment,
                reviewTestData.readPages);

        ReviewsResults reviewResponse = api.review.postReview(requestBody, actualAccess);

        String localStorageAuthBody = step("Создание JSON авторизации для localStorage", () -> {
            User user = new User(
                    userInfoResponse.id(),
                    userInfoResponse.username(),
                    userInfoResponse.firstName(),
                    userInfoResponse.lastName(),
                    userInfoResponse.email(),
                    userInfoResponse.remoteAddr()
            );
            AuthModel auth = new AuthModel(user, actualAccess, actualRefresh, true);
            return new ObjectMapper().writeValueAsString(auth);
        });

        reviewPage.openPageWithLocalStorage(localStorageAuthBody)
                .openClubPageById(clubResponse.id())
                .checkReview(reviewTestData.reviewData, BaseTestData.username, reviewTestData.assessment, reviewTestData.readPages);

        api.review.deleteReview(reviewResponse.id(), actualAccess);

        reviewPage.openPageWithLocalStorage(localStorageAuthBody)
                .openClubPageById(clubResponse.id())
                .checkEmtpyReviewList();
    }
}
