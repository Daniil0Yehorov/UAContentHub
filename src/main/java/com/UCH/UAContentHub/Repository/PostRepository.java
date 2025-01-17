package com.UCH.UAContentHub.Repository;


import com.UCH.UAContentHub.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Integer> {
    @Query("SELECT p FROM Post p JOIN Subscription s ON s.creator = p.profile.user WHERE s.user.id = :userId ORDER BY p.publishDate DESC")
    List<Post> findBySubscribedCreatorsOrderByPublishDateDesc(@Param("userId") int userId);
    List<Post> findByProfileUserId(int userId);
}
