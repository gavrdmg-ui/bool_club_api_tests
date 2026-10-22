package tests.ui;

import api.ApiClient;
import com.codeborne.selenide.Configuration;
import helpers.Attach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import pages.ReviewClubPage;
import io.restassured.RestAssured;
import testdata.ClubsTestData;
import testdata.ReviewsTestData;


import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class TestBase {
    ReviewClubPage reviewPage = new ReviewClubPage();
    ApiClient api = new ApiClient();
    public ClubsTestData clubsTestData = new ClubsTestData();
    public ReviewsTestData reviewTestData = new ReviewsTestData();


    @BeforeAll
    static void setUp() {
        Configuration.browser=System.getProperty("browser","chrome");
        Configuration.browserVersion=System.getProperty("browserVersion","156.0");
        Configuration.browserSize = System.getProperty("browserSize","1920x1080");
        Configuration.headless= Boolean.parseBoolean(System.getProperty("headless","false"));
        Configuration.holdBrowserOpen = false;
        Configuration.baseUrl = System.getProperty("baseUrl","https://book-club.qa.guru");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments(List.of("--disable-dev-shm-usage", "--no-sandbox"));
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                "enableVNC", true,
                "enableVideo", true
        ));
        Configuration.browserCapabilities = capabilities;
        Configuration.remote = System.getProperty("remoteURL","https://user1:1234@selenoid.qa.guru/wd/hub");

        RestAssured.baseURI = System.getProperty("baseUrl","https://book-club.qa.guru");

    }


    @AfterEach
    void tearDown() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.attachAsText("Some file", "Some content");
        Attach.browserConsoleLogs();
        Attach.addVideo();
        closeWebDriver();
    }
}