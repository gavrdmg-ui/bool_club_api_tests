package testdata;

public class UpdateTestData extends BaseTestData {
    public String firstName = faker.name().firstName();
    public String lastName = faker.name().lastName();
    public String email = faker.internet().emailAddress();
    public static String expectedDetailErrorWithoutAuthToken = "Authentication credentials were not provided.";
}
