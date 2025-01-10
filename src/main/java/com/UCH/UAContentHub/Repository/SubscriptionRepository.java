package com.UCH.UAContentHub.Repository;


import com.UCH.UAContentHub.Entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {

    Subscription findByCreator_IdAndUser_Id(int creatorId, int UserId);
}
