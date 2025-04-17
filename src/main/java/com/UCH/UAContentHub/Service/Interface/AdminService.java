package com.UCH.UAContentHub.Service.Interface;


import com.UCH.UAContentHub.Entity.Complaint;
import com.UCH.UAContentHub.Entity.Enum.ComplaintStatus;
import com.UCH.UAContentHub.Entity.Enum.ReviewStatus;
import com.UCH.UAContentHub.Entity.Review;
import com.UCH.UAContentHub.Entity.User;
import com.UCH.UAContentHub.Entity.Enum.CreatorProfileStatus;
import java.util.List;

public interface AdminService {
    void deleteUnconfirmedCreators();
    void deleteOldNotApprovedReviews();
    void deleteCreator(int userID);
    List<User> getPendingCreators();
    User getCreatorById(int id);
    void changeCreatorStatus(int id, CreatorProfileStatus status);
    List<Review> getPendingReviews();
    Review getReviewById(int id);
    void changeReviewStatus(int id, ReviewStatus status);
    List<Complaint> getPendingComplaints();
    Complaint getComplaintById(int id);
    void changeComplaintStatus(int id, ComplaintStatus status);
}
