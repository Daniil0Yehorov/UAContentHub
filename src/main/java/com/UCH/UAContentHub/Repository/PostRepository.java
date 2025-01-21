package com.UCH.UAContentHub.Repository;


import com.UCH.UAContentHub.Entity.Post;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Integer> {
    @Query("SELECT p FROM Post p WHERE p.profile.user.id IN (SELECT s.creator.id FROM Subscription s WHERE s.user.id = :userId) ORDER BY p.publishDate DESC")
    List<Post> findBySubscribedCreatorsOrderByPublishDateDesc(@Param("userId") int userId);
    @Query("SELECT p FROM Post p WHERE p.profile.user.id = :userId ORDER BY p.publishDate DESC")
    List<Post> findPostsByUserOrderByPublishDateDesc(@Param("userId") int userId);
   // List<Post> findByProfileUserId(int userId);
    @Transactional
    @Modifying
    @Query("DELETE FROM Post p WHERE p.id = :id")
    void deletePostById(@Param("id") int id);
}
