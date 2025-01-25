package com.UCH.UAContentHub.Repository;

import com.UCH.UAContentHub.Entity.Post_has_Image;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface Post_has_ImageRepository extends JpaRepository<Post_has_Image,Integer> {
    List<Post_has_Image> findByPostId(int postId);
}
