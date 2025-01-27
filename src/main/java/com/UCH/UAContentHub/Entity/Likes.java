package com.UCH.UAContentHub.Entity;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "UserID", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "PostID", referencedColumnName = "id", nullable = false)
    private Post post;

    @Column(nullable = false,name = "Like_Date")
    private LocalDateTime LikeDate;

    public Likes(int id, User user, Post post, LocalDateTime likeDate) {
        this.id = id;
        this.user = user;
        this.post = post;
        this.LikeDate = likeDate;
    }

    public Likes(){}
}
