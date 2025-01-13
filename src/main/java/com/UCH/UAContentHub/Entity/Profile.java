package com.UCH.UAContentHub.Entity;


import com.UCH.UAContentHub.Entity.Enum.CreatorProfileStatus;
import com.UCH.UAContentHub.Entity.Enum.User_Status;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "UserID")
    private User user;

    @Column(nullable = false,length = 65535)
    private String Description;

    @Enumerated(EnumType.STRING)
    @Column( name = "Status",nullable = false)
    private CreatorProfileStatus status;

    private String AvatarURL;

    @Column( length = 65535)
    private String Tiktok;

    @Column( length = 65535)
    private String Instagram;

    @Column(length = 65535)
    private String Twitch;

    @Column( length = 65535)
    private String Youtube;
    @Column( name = "Rating")
    private int rating;
    @Column(name="Subscribers_Count")
    private int SubscribersCount;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @OneToMany(mappedBy = "profile")
    private Set<Profile_has_tags> phs;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    private List<Complaint> complaints;



    public CreatorProfileStatus getStatus() {
        return status;
    }

    public void setStatus(CreatorProfileStatus status) {
        this.status = status;
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

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public String getAvatarURL() {
        return AvatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.AvatarURL = avatarURL;
    }

    public String getTiktok() {
        return Tiktok;
    }

    public void setTiktok(String tiktok) {
        this.Tiktok = tiktok;
    }

    public String getInstagram() {
        return Instagram;
    }

    public void setInstagram(String instagram) {
        this.Instagram = instagram;
    }

    public String getTwitch() {
        return Twitch;
    }

    public void setTwitch(String twitch) {
        this.Twitch = twitch;
    }

    public String getYoutube() {
        return Youtube;
    }

    public void setYoutube(String youtube) {
        this.Youtube = youtube;
    }

    public int getSubscribersCount() {
        return SubscribersCount;
    }

    public void setSubscribersCount(int subscribersCount) {
        this.SubscribersCount = subscribersCount;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Set<Profile_has_tags> getPhs() {
        return phs;
    }

    public void setPhs(Set<Profile_has_tags> phs) {
        this.phs = phs;
    }

    public List<Complaint> getComplaints() {
        return complaints;
    }

    public void setComplaints(List<Complaint> complaints) {
        this.complaints = complaints;
    }


    public Profile(){}
}
