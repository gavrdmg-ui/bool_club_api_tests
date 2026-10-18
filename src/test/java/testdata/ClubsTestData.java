package testdata;

public class ClubsTestData extends BaseTestData {
    public static String expectedNextPagination = "/api/v1/clubs/?page=2";
    public String bookTitle = faker.book().title() + faker.number().numberBetween(1000, 9999);
    public String bookAuthors = faker.book().author();
    public int publicationYear = faker.number().numberBetween(1000, 9999);
    public String description = faker.book().publisher();
    public String telegramChatLink = "https://t.me/qa_guru_" + faker.number().numberBetween(0, 9999);
    public String newBookTitle = faker.book().title() + faker.number().numberBetween(1000, 9999);
    public String newBookAuthors = faker.book().author();
    public int newPublicationYear = faker.number().numberBetween(1000, 9999);
    public String newDescription = faker.book().publisher();
    public String newTelegramChatLink = "https://t.me/qa_guru_" + faker.number().numberBetween(0, 9999);
    public static String notFoundClubError = "No Club matches the given query.";
}
