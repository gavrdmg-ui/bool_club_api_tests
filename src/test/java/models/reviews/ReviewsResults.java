package models.reviews;

public record ReviewsResults(Integer id,
                             Integer club,
                             ReviewsUser user,
                             String review,
                             Integer assessment,
                             Integer readPages,
                             String created,
                             String modified) {
}
