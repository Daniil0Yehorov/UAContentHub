package com.UCH.UAContentHub.Entity;


import com.UCH.UAContentHub.Entity.Enum.ComplaintStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "UserID", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "PostID", referencedColumnName = "id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "profile_UserID", referencedColumnName = "UserID")
    private Profile profile;

    @Column(length = 65535, nullable = false)
    private String Reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", nullable = false)
    private ComplaintStatus status;

    public Complaint(int id, User user, Post post,
                     Profile profile, String reason, ComplaintStatus status) {
        this.id = id;
        this.user = user;
        this.post = post;
        this.profile = profile;
        this.Reason = reason;
        this.status = status;
    }

    public Complaint(){}
}