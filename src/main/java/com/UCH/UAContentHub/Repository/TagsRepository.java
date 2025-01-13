package com.UCH.UAContentHub.Repository;


import com.UCH.UAContentHub.Entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Set;

public interface TagsRepository extends JpaRepository<Tags,Integer> {

    List<Tags> findByNameIn(Set<String> names);
}
