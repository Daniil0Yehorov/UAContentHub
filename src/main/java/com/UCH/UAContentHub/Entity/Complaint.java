package com.UCH.UAContentHub.Entity;


import com.UCH.UAContentHub.Entity.Enum.ComplaintStatus;
import jakarta.persistence.*;

@Entity
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "PostID")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "profile_UserID")
    private Profile profile;

    @Column(length = 65535, nullable = false)
    private String Reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplaintStatus Status;

    public Complaint(int id, User user, Post post,
                     Profile profile, String reason, ComplaintStatus status) {
        this.id = id;
        this.user = user;
        this.post = post;
        this.profile = profile;
        this.Reason = reason;
        this.Status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        this.Reason = reason;
    }

    public ComplaintStatus getStatus() {
        return Status;
    }

    public void setStatus(ComplaintStatus status) {
        this.Status = status;
    }

    public Complaint(){}
}