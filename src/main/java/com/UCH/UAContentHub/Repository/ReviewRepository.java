package com.UCH.UAContentHub.Repository;


import com.UCH.UAContentHub.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Integer> {
    boolean existsByUserIdAndCreatorId(int userId, int creatorId);
    Review findByUserIdAndCreatorId(int userId, int creatorId);
    List<Review> findAllByCreatorId(int creatorId);
}
