package testdata;

import net.datafaker.Faker;

public class BaseTestData {
    public Faker faker = new Faker();
    public static String expectedRequiredError = "This field is required.";
    public static String expectedBlankError = "This field may not be blank.";
    public static String username = "Amie";
    public static String password = "Reiko";
    public static String ipAddrRegexp = "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}"
            + "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$";
}
