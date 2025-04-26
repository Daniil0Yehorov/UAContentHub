package com.UCH.UAContentHub.Repository;


import com.UCH.UAContentHub.Entity.Enum.CreatorProfileStatus;
import com.UCH.UAContentHub.Entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile,Integer> {
    List<Profile> findByStatus(CreatorProfileStatus status);

    @Query("SELECT p FROM Profile p WHERE p.Tiktok = :tiktok")
    Profile findByTiktok(@Param("tiktok") String tiktok);

    @Query("SELECT p FROM Profile p WHERE p.Instagram = :instagram")
    Profile findByInstagram(@Param("instagram") String instagram);

    @Query("SELECT p FROM Profile p WHERE p.Twitch = :twitch")
    Profile findByTwitch(@Param("twitch") String twitch);

    @Query("SELECT p FROM Profile p WHERE p.Youtube = :youtube")
    Profile findByYoutube(@Param("youtube") String youtube);

}
