package com.UCH.UAContentHub.Service.Implementation;

import com.UCH.UAContentHub.Entity.Review;
import com.UCH.UAContentHub.Entity.Enum.ReviewStatus;
import com.UCH.UAContentHub.Repository.ReviewRepository;
import com.UCH.UAContentHub.Service.Interface.ReviewService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private ReviewRepository reviewRepository;

    @Override
    public Review createReview(Review review) {

        if (reviewRepository.existsByUserIdAndCreatorId(review.getUser().getId(), review.getCreator().getId())) {
            throw new IllegalArgumentException("Ви вже залишили відгук цьому креатору. Ви можете його оновити або видалити.");
        }
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new IllegalArgumentException("Рейтинг може бути в діапазоні від 1 до 5");
        }

        if (review.getUser() == null || review.getUser().getId() <= 0) {
            throw new IllegalArgumentException("помилка з користувачем");
        }

        if (review.getCreator() == null || review.getCreator().getId() <= 0) {
            throw new IllegalArgumentException("помилка з креатором");
        }

        if (review.getReviewDate() == null) {
            review.setReviewDate(LocalDateTime.now());
        }

        if (review.getStatus() == null) {
            review.setStatus(ReviewStatus.PENDING);
        }

        return reviewRepository.save(review);
    }
    @Transactional
    @Override
    public void deleteReview(int reviewID) {
        Review review = reviewRepository.findById(reviewID)
                .orElseThrow(() -> new IllegalArgumentException("Відгук не знайдено або він скоріш вже видален."));
        if (review.getStatus() == ReviewStatus.NOT_APPROVED) {
            reviewRepository.deleteById(reviewID);
        } else {
            throw new IllegalArgumentException("Ви не можете видалити цей відгук, оскільки його статус не 'NOT_APPROVED'.");
        }
    }
    //в теорії спрацює, але не перевірити правильність поки немає адмін панелі
    @Override
    public void updateReview(Review review) {
        if (reviewRepository.existsById(review.getId())) {
            reviewRepository.save(review);
        } else {
            throw new IllegalArgumentException("Відгук не знайден.");
        }
    }
    public Review getReviewByUserAndCreator(int userId, int creatorId) {
        return reviewRepository.findByUserIdAndCreatorId(userId, creatorId);
    }
    @Override
    public List<Review> getReviewsByCreator(int creatorId) {
        return reviewRepository.findAllByCreatorId(creatorId);
    }
}
