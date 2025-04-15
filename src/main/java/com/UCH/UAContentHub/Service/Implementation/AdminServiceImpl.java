package com.UCH.UAContentHub.Service.Implementation;

import com.UCH.UAContentHub.Entity.User;
import com.UCH.UAContentHub.Repository.UserRepository;
import com.UCH.UAContentHub.Service.Interface.AdminService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public void deleteUnconfirmedCreators() {//процедура з бд
        Query query = entityManager.createNativeQuery("CALL DeleteUnconfirmedUsers");//название поменять функции
        query.executeUpdate();
    }
    //код процедури
    /*DELIMITER $$
CREATE PROCEDURE DeleteUnconfirmedUsers()
BEGIN
    DELETE u
    FROM uacontenthub.User u
    JOIN uacontenthub.Profile p ON u.id = p.UserID
    WHERE p.Status = 'UNCONFIRMED'
    AND u.Registration_Date < CURDATE() - INTERVAL 30 DAY;
END$$

DELIMITER ;

DELIMITER $$
 */

    @Override
    @Transactional
    public void deleteOldNotApproveReviews() {//процедура з бд
        Query query = entityManager.createNativeQuery("CALL DeleteOldNotApprovedReviews()");//название поменять функции
        query.executeUpdate();
    }
    //код процедури
    /*
    DELIMITER $$

-- Процедура для видалення непідтверджених рецензій (після 7 днів)
    CREATE PROCEDURE DeleteOldNotApprovedReviews()
    BEGIN
    DELETE r
    FROM uacontenthub.Review r
    WHERE r.Status = 'NOT_APPROVED'
    AND r.Review_Date < CURDATE() - INTERVAL 7 DAY;
    END$$

            DELIMITER ;*/

    @Override//ручне видалення, а Є ще автоматичне при 5 скаргах
    @Transactional
    public void deleteAuthor(int userID) {

        User user = userRepository.findById(userID).orElse(null);
        if(user!=null){
            userRepository.delete(user);
        }
        //мб краще через процедуру
        Query query = entityManager.createNativeQuery("CALL DELETEAuthor(:userID)");
        query.setParameter("userID", userID);
        query.executeUpdate();
    }
    //код процедури
    /*
    DELIMITER $$

CREATE PROCEDURE DELETEAuthor(IN userID INT)
BEGIN
    DELETE FROM User WHERE id = userID;
END$$

DELIMITER ;
     */

    @Override
    public List<User> getPendingCreators() {
        return userRepository.findPendingCreators();
    }
    @Override
    public User getCreatorById(int id){
        return  userRepository.findById(id).orElse(null);
    }


}
