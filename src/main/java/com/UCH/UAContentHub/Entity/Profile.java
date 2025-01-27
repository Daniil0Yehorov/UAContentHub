package com.UCH.UAContentHub.Entity;


import com.UCH.UAContentHub.Entity.Enum.CreatorProfileStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Profile", uniqueConstraints = {
        @UniqueConstraint(name = "AvatarURL_UNIQUE", columnNames = "AvatarURL")
})
@Getter
@Setter
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "UserID", referencedColumnName = "id")
    private User user;

    @Column(nullable = false,length = 65535)
    private String Description;

    @Enumerated(EnumType.STRING)
    @Column( name = "Status",nullable = false)
    private CreatorProfileStatus status;

    @Column(nullable = false)
    private String AvatarURL;

    @Column( length = 65535)
    private String Tiktok;

    @Column( length = 65535)
    private String Instagram;

    @Column(length = 65535)
    private String Twitch;

    @Column( length = 65535)
    private String Youtube;
    @Column(name = "Rating")
    private float rating;

    @Column(name="Subscribers_Count")
    private int SubscribersCount;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Post> posts;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Profile_has_tags> phs;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Complaint> complaints;

    public Profile(){}
}
