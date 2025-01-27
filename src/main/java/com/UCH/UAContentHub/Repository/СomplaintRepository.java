package com.UCH.UAContentHub.Repository;


import com.UCH.UAContentHub.Entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface СomplaintRepository extends JpaRepository<Complaint,Integer> {
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Complaint c " +
            "WHERE c.user.id = :userId AND c.profile.user.id = :creatorId")
    boolean existsComplaintByUserAndProfile(@Param("userId") int userId,
                                            @Param("creatorId") int creatorId);
}
