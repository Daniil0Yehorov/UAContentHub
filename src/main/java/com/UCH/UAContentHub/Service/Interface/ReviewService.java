package com.UCH.UAContentHub.Service.Interface;

import java.util.List;
import com.UCH.UAContentHub.Entity.Review;

public interface ReviewService {
    Review createReview(Review review);
    void deleteReview(int reviewID);//якщо статус нот апрувд, користувач може видалити
    void updateReview(Review review);//оновлення статусу адміністратором
    Review getReviewByUserAndCreator(int userId, int creatorId);
    List<Review> getReviewsByCreator(int creatorId);//Список рецензій на певного автора
}
