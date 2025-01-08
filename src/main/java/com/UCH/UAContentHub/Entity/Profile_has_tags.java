package com.UCH.UAContentHub.Entity;


import jakarta.persistence.*;

@Entity
public class Profile_has_tags {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "profile_UserID")
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "tags_id")
    private Tags tags;

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Tags getTags() {
        return tags;
    }

    public void setTags(Tags tags) {
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Profile_has_tags(int id, Profile profile, Tags tags) {
        this.id = id;
        this.profile = profile;
        this.tags = tags;
    }
    public Profile_has_tags(){}
}
