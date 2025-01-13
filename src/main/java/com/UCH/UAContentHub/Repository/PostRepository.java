package com.UCH.UAContentHub.Repository;


import com.UCH.UAContentHub.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Integer> {
}
