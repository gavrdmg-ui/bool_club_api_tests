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
    public static String expectedDetailErrorWithoutAuthToken = "Authentication credentials were not provided.";
    public static String dateRegex = "^\\d{4}-\\d{2}-\\d{2}[T ]\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?(?:Z|[+-]\\d{2}:?\\d{2})$";
    public static String noPermissionError="You do not have permission to perform this action.";

}
