package testdata;

public class ReviewsTestData extends BaseTestData {
    public Integer clubWithReviewsId = 2;
    public Integer baseReviewsPaginationSize = 50;
    public String paginationUrl = "http://api.example.org/accounts/?page=";
    public String reviewData = faker.book().title();
    public Integer assessment = faker.number().numberBetween(1, 5);
    public Integer readPages = faker.number().numberBetween(1, 300);
    public String notFoundReviewError = "No BookReview matches the given query.";

    public String otherUserUserName = "SamanthaRoselee";
    public String otherUserUserPassword = "Kasey";
    public String newReviewData = faker.book().title();
    public Integer newAssessment = faker.number().numberBetween(1, 5);
    public Integer newReadPages = faker.number().numberBetween(1, 300);
    public static String emptyReviewMessage = "Пока нет отзывов. Будьте первым, кто поделится впечатлениями!";
}
