package tests;

import io.restassured.response.Response;
import models.WithoutAuthTokenResponseModel;
import models.clubs.ClubModel;
import models.clubs.ClubsListResponseModel;
import models.clubs.CreateClubBodyModel;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testdata.BaseTestData;
import testdata.ClubsTestData;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.clubs.ClubsSpec.notFoundClubsResponseSpec;

public class ClubsTests extends TestBase {

    public ClubsTestData testData;

    @BeforeEach
    public void prepareTestData() {
        testData = new ClubsTestData();
    }

    @Test
    public void successfulGetClubsTest() {
        ClubsListResponseModel response = api.clubs.getClubs();

        step("Проверяем наличие пагинации", () -> {
            assertThat(response).isNotNull();
            if (response.results().size() < response.count()) {
                assertThat(response.next()).endsWith(ClubsTestData.expectedNextPagination);
            } else assertThat(response.results().size()).isEqualTo(response.count());
        });

        step("Проверяем обязательные поля для для каждого club в results", () -> {
            for (ClubModel club : response.results()) {
                assertThat(club.id()).isNotNull().isPositive();
                assertThat(club.bookTitle()).isNotBlank();
                assertThat(club.bookAuthors()).isNotBlank();
                assertThat(club.publicationYear()).isNotNull();
                assertThat(club.description()).isNotNull();
                assertThat(club.telegramChatLink()).isNotNull();
                assertThat(club.owner()).isNotNull().isPositive();
                assertThat(club.members()).isNotNull();
                assertThat(club.reviews()).isNotNull();
                assertThat(club.created()).isNotNull();
            }
        });
    }

    @Test
    public void successfulCreateClubTest() {
        LoginBodyModel loginData = new LoginBodyModel(BaseTestData.username, BaseTestData.password);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);

        String actualAccess = loginResponse.access();

        CreateClubBodyModel createData = new CreateClubBodyModel(
                testData.bookTitle,
                testData.bookAuthors,
                testData.publicationYear,
                testData.description,
                testData.telegramChatLink
        );
        ClubModel response = api.clubs.createClub(createData, actualAccess);
        step("Проверяем что поля клуба заполнены корректно", () -> {
            assertThat(response.bookTitle()).isEqualTo(testData.bookTitle);
            assertThat(response.bookTitle()).isEqualTo(testData.bookTitle);
            assertThat(response.bookTitle()).isEqualTo(testData.bookTitle);
            assertThat(response.bookTitle()).isEqualTo(testData.bookTitle);
            assertThat(response.bookTitle()).isEqualTo(testData.bookTitle);
            assertThat(response.created()).containsPattern(ClubsTestData.dateRegex);
            assertThat(response.modified()).isNull();
        });
    }

    @Test
    public void unauthorizedCreateClubTest() {

        CreateClubBodyModel createData = new CreateClubBodyModel(
                testData.bookTitle,
                testData.bookAuthors,
                testData.publicationYear,
                testData.description,
                testData.telegramChatLink
        );
        WithoutAuthTokenResponseModel response = api.clubs.createClub(createData);
        step("Проверяем текст ошибки", () -> {
            assertThat(response.detail()).isEqualTo(BaseTestData.expectedDetailErrorWithoutAuthToken);
        });
    }

    @Test
    public void updateClubTest() {
        LoginBodyModel loginData = new LoginBodyModel(BaseTestData.username, BaseTestData.password);

        String actualAccess = api.auth.login(loginData).access();

        CreateClubBodyModel createData = new CreateClubBodyModel(
                testData.bookTitle,
                testData.bookAuthors,
                testData.publicationYear,
                testData.description,
                testData.telegramChatLink
        );

        int clubId = api.clubs.createClub(createData, actualAccess).id();

        CreateClubBodyModel updateData = new CreateClubBodyModel(
                testData.newBookTitle,
                testData.newBookAuthors,
                testData.newPublicationYear,
                testData.newDescription,
                testData.newTelegramChatLink
        );
        ClubModel response = api.clubs.updateclub(updateData, actualAccess, clubId);

        step("Проверяем что поля клуба обновлены корректно", () -> {
            assertThat(response.bookTitle()).isEqualTo(testData.newBookTitle);
            assertThat(response.bookAuthors()).isEqualTo(testData.newBookAuthors);
            assertThat(response.publicationYear()).isEqualTo(testData.newPublicationYear);
            assertThat(response.description()).isEqualTo(testData.newDescription);
            assertThat(response.telegramChatLink()).isEqualTo(testData.newTelegramChatLink);
            assertThat(response.modified()).isGreaterThan(response.created());
        });
    }

    @Test
    public void deleteClubTest() {
        LoginBodyModel loginData = new LoginBodyModel(BaseTestData.username, BaseTestData.password);

        String actualAccess = api.auth.login(loginData).access();

        CreateClubBodyModel createData = new CreateClubBodyModel(
                testData.bookTitle,
                testData.bookAuthors,
                testData.publicationYear,
                testData.description,
                testData.telegramChatLink
        );

        int clubId = api.clubs.createClub(createData, actualAccess).id();

        step("Удаляем клуб", () -> api.clubs.deleteClub(clubId, actualAccess));

        step("Проверка что клуб удален", () -> {
            Response response = api.clubs.getClubsById(clubId, notFoundClubsResponseSpec);
            assertThat(response.path("detail").toString()).isEqualTo(ClubsTestData.notFoundClubError);
        });
    }

}
