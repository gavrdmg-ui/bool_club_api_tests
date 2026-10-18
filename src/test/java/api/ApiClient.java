package api;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;

public class ApiClient {

    public final AuthApiClient auth = new AuthApiClient();
    public final UsersApiClient users = new UsersApiClient();
    public final ClubsApiClient clubs = new ClubsApiClient();
    public final ReviewsApiClient review = new ReviewsApiClient();

    public static ResponseSpecification successfulDeleteResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(204)
            .build();

}
