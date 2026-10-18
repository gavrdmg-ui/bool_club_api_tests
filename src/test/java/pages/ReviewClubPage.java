package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import testdata.ReviewsTestData;


import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class ReviewClubPage {

    private final SelenideElement reviewCard = $(".review-card.user-review");


    @Step("Открытие страницы с локал сторедж")
    public ReviewClubPage openPageWithLocalStorage(String localStorageAuthBody) {
        open("/favicon.svg");
        localStorage().setItem("book_club_auth", localStorageAuthBody);

        return this;
    }

    @Step("Открытие страницы клуба по id")
    public ReviewClubPage openClubPageById(Integer clubId) {
        open(String.format("/clubs/%d", clubId));
        return this;
    }

    @Step("Проверка полей отзыва")
    public ReviewClubPage checkReview(String reviewData, String userName, Integer assessment, Integer readPages) {
        reviewCard.$(".reviewer-name").shouldHave(text(userName));
        reviewCard.$(".stars").shouldHave(matchText("^★{" + assessment + "}☆*$"));
        reviewCard.$(".read-pages").shouldHave(text(readPages.toString()));
        reviewCard.$(".review-content").shouldHave(text(reviewData));
        return this;
    }

    @Step("Проверка удаления отзыва")
    public ReviewClubPage checkEmtpyReviewList() {
        $(".no-reviews").shouldHave(text(ReviewsTestData.emptyReviewMessage));
        return this;
    }

}
