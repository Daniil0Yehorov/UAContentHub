package com.UCH.UAContentHub.Entity;


import com.UCH.UAContentHub.Entity.Enum.ComplaintStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "UserPost", columnNames = {"UserID", "PostID"}),
                @UniqueConstraint(name = "UserProfile", columnNames = {"UserID", "profile_UserID"})
        }
)
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "UserID", referencedColumnName = "id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "PostID", referencedColumnName = "id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "profile_UserID", referencedColumnName = "UserID",nullable = false)
    private Profile profile;

    @Column(length = 65535, nullable = false)
    private String Reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", nullable = false)
    private ComplaintStatus status;

    @Column(nullable = false,name = "Complaint_Date")
    private LocalDateTime ComplaintDate;

    public Complaint(){}
}