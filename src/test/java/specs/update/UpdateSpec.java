package specs.update;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.notNullValue;
import static specs.BaseSpec.baseRequestSpec;

public class UpdateSpec {

    public static RequestSpecification updateRequestSpec = baseRequestSpec;

    public static ResponseSpecification successfulUpdateResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/update/successful_update_response_schema.json"))
            .expectBody("id", notNullValue())
            .build();

    public static ResponseSpecification updateWithoutAuthTokenResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(401)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/update/update_without_auth_token_response_schema.json"))
            .expectBody("detail", notNullValue())
            .build();
}
