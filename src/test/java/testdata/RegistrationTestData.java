package testdata;

public class RegistrationTestData extends BaseTestData {
    public String username = faker.name().firstName();
    public String password = faker.name().firstName();
    public static String expectedExistUserError = "A user with that username already exists.";
}
