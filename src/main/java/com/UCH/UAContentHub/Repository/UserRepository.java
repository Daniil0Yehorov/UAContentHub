package com.UCH.UAContentHub.Repository;


import com.UCH.UAContentHub.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByLogin(String login);
    @Query("SELECT u FROM User u WHERE u.Email = :Email")
    User findByEmail(String Email);
    @Query("SELECT u FROM User u WHERE u.role = 'CREATOR' AND u.profile.status = 'PENDING'")
    List<User> findPendingCreators();
}
