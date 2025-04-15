package com.UCH.UAContentHub.Service.Interface;


import com.UCH.UAContentHub.Entity.User;
import java.util.List;

public interface AdminService {
    void deleteUnconfirmedCreators();
    void deleteOldNotApproveReviews();
    void deleteAuthor(int userID);
    List<User> getPendingCreators();
    User getCreatorById(int id);
}
