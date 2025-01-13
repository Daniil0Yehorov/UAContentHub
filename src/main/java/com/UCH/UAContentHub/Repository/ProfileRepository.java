package com.UCH.UAContentHub.Repository;


import com.UCH.UAContentHub.Entity.Enum.CreatorProfileStatus;
import com.UCH.UAContentHub.Entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile,Integer> {
    Profile getProfileById(int id);

    List<Profile> findByStatus(CreatorProfileStatus status);

    List<Profile> findByStatusAndRatingBetween(CreatorProfileStatus  Status, int minRating, int maxRating);
}
