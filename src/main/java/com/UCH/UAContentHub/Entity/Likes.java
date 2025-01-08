package com.UCH.UAContentHub.Entity;



import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "PostID", nullable = false)
    private Post post;

    @Column(nullable = false,name = "Like_Date")
    private LocalDateTime LikeDate;

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

    public LocalDateTime getLikeDate() {
        return LikeDate;
    }

    public void setLikeDate(LocalDateTime likeDate) {
        this.LikeDate = likeDate;
    }

    public Likes(int id, User user, Post post, LocalDateTime likeDate) {
        this.id = id;
        this.user = user;
        this.post = post;
        this.LikeDate = likeDate;
    }

    public Likes(){}
}
