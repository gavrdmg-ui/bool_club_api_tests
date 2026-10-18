package tests.api;

import api.ApiClient;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class TestBase {
    ApiClient api = new ApiClient();

    @BeforeAll
    public static void setUp() {

        RestAssured.baseURI = "https://book-club.qa.guru";
    }
}
