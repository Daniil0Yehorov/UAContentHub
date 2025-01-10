package com.UCH.UAContentHub.Repository;


import com.UCH.UAContentHub.Entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagsRepository extends JpaRepository<Tags,Long> {
    Tags findByName(String name);
    @Query("SELECT t FROM Tags t WHERE t.name IN :names")
    List<Tags> findByNameIn(@Param("names") List<String> names);
}
