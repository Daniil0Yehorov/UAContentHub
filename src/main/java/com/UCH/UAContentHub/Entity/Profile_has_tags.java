package com.UCH.UAContentHub.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Profile_has_tags {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "profile_UserID", referencedColumnName = "UserID")
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "tags_id", referencedColumnName = "id")
    private Tags tags;

    public Profile_has_tags(int id, Profile profile, Tags tags) {
        this.id = id;
        this.profile = profile;
        this.tags = tags;
    }
    public Profile_has_tags(){}
}
