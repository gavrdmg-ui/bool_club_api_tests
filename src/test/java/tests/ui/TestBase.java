package tests.ui;

import api.ApiClient;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import pages.ReviewClubPage;
import io.restassured.RestAssured;
import testdata.ClubsTestData;
import testdata.ReviewsTestData;


import static com.codeborne.selenide.Selenide.closeWebDriver;

public class TestBase {
    ReviewClubPage reviewPage = new ReviewClubPage();
    ApiClient api = new ApiClient();
    public ClubsTestData clubsTestData = new ClubsTestData();
    public ReviewsTestData reviewTestData = new ReviewsTestData();


    @BeforeAll
    static void setUp() {
        Configuration.baseUrl = "https://book-club.qa.guru";
        Configuration.browserSize = "1920x1080";
        Configuration.holdBrowserOpen = true;
        RestAssured.baseURI = "https://book-club.qa.guru";

    }


    @AfterEach
    void tearDown() {
        closeWebDriver();
    }
}