package com.UCH.UAContentHub.Repository;


import com.UCH.UAContentHub.Entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes,Integer> {
    boolean existsByPostIdAndUserId(int postId, int userId);
    Optional<Likes> findByPostIdAndUserId(int postId, int userId);
}
