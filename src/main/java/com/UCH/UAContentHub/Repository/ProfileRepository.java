package com.UCH.UAContentHub.Repository;


import com.UCH.UAContentHub.Entity.Enum.CreatorProfileStatus;
import com.UCH.UAContentHub.Entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile,Integer> {
    List<Profile> findByStatus(CreatorProfileStatus status);
    List<Profile> findByStatusAndRatingBetween(CreatorProfileStatus  Status, int minRating, int maxRating);

    @Query("SELECT COUNT(p) > 0 FROM Profile p WHERE p.Tiktok = :tiktok")
    boolean existsByTiktok(String tiktok);

    @Query("SELECT COUNT(p) > 0 FROM Profile p WHERE p.Instagram = :instagram")
    boolean existsByInstagram(String instagram);

    @Query("SELECT COUNT(p) > 0 FROM Profile p WHERE p.Twitch = :twitch")
    boolean existsByTwitch(String twitch);

    @Query("SELECT COUNT(p) > 0 FROM Profile p WHERE p.Youtube = :youtube")
    boolean existsByYoutube(String youtube);
}
