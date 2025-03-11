package com.UCH.UAContentHub.Entity;


import jakarta.persistence.*;
import com.UCH.UAContentHub.Entity.Enum.ReviewStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "UserID", referencedColumnName = "id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "CreatorID", referencedColumnName = "id", nullable = false)
    private User creator;

    @Column(length = 65535)
    private String Text;

    @Column(nullable = false)
    private int Rating;

    @Column(nullable = false,name = "Review_Date")
    private LocalDateTime ReviewDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewStatus Status;

    public Review() {}
}