package com.UCH.UAContentHub.Service.Implementation;

import com.UCH.UAContentHub.Entity.Complaint;
import com.UCH.UAContentHub.Entity.Enum.ComplaintStatus;
import com.UCH.UAContentHub.Entity.Enum.CreatorProfileStatus;
import com.UCH.UAContentHub.Entity.Enum.ReviewStatus;
import com.UCH.UAContentHub.Entity.Review;
import com.UCH.UAContentHub.Entity.User;
import com.UCH.UAContentHub.Repository.ReviewRepository;
import com.UCH.UAContentHub.Repository.UserRepository;
import com.UCH.UAContentHub.Repository.ComplaintRepository;
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
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    ComplaintRepository complaintRepository;


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
    public void deleteOldNotApprovedReviews() {//процедура з бд
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
    public void deleteCreator(int userID) {

        User user = userRepository.findById(userID).orElse(null);
        if(user!=null){
            userRepository.delete(user);
        }
        Query query = entityManager.createNativeQuery("CALL DELETECREATOR(:userID)");
        query.setParameter("userID", userID);
        query.executeUpdate();
    }
    //код процедури
    /*
    DELIMITER $$

CREATE PROCEDURE DELETECREATOR(IN userID INT)
BEGIN
    DELETE FROM User WHERE id = userID;
END$$

DELIMITER ;
     */
    @Override
    public void changeCreatorStatus(int id, CreatorProfileStatus status) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Користувача не знайдено"));
        user.getProfile().setStatus(status);
        userRepository.save(user);
    }

    @Override
    public List<User> getPendingCreators() {
        return userRepository.findPendingCreators();
    }
    @Override
    public User getCreatorById(int id){
        return  userRepository.findById(id).orElse(null);
    }
    @Override
    public List<Review> getPendingReviews() {
        return reviewRepository.findPendingReviews();
    }
    @Override
    public Review getReviewById(int id){
        return  reviewRepository.findById(id).orElse(null);
    }
    @Override
    public void changeReviewStatus(int id, ReviewStatus status) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("рецензії не знайдено"));
        review.setStatus(status);
        reviewRepository.save(review);
    }
    @Override
    public List<Complaint> getPendingComplaints() {
        return complaintRepository.findPendingComplaints();
    }
    @Override
    public Complaint getComplaintById(int id){
        return  complaintRepository.findById(id).orElse(null);
    }

    @Override
    public void changeComplaintStatus(int id, ComplaintStatus status) {
        Complaint complaint = complaintRepository.findById(id).orElseThrow(() -> new RuntimeException("скарги не знайдено"));
        complaint.setStatus(status);
        complaintRepository.save(complaint);
    }
}
