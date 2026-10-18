package models.reviews;


import java.util.List;

public record ReviewsModel(Integer count,
                           String next,
                           String previous,
                           List<ReviewsResults> results
) {
}
