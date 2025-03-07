package com.UCH.UAContentHub.Entity;


import com.UCH.UAContentHub.Entity.Enum.CreatorProfileStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Profile", uniqueConstraints = {
        @UniqueConstraint(name = "AvatarURL_UNIQUE", columnNames = "AvatarURL"),
        @UniqueConstraint(name = "TikTok_UNIQUE", columnNames = "TikTok"),
        @UniqueConstraint(name = "Instagram_UNIQUE", columnNames = "Instagram"),
        @UniqueConstraint(name = "Twitch_UNIQUE", columnNames = "Twitch"),
        @UniqueConstraint(name = "Youtube_UNIQUE", columnNames = "Youtube")
})
@Getter
@Setter
public class Profile {
    /*@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,name = "id")
    private int id;*///old pkfk

    @Id//new  npkfk
    @Column(name = "UserID")//new  npkfk
    private int id;//new pkfk

    @OneToOne
    @MapsId//new  pkfk
    @JoinColumn(name = "UserID", referencedColumnName = "id")
    private User user;

    @Column(nullable = false,length = 65535)
    private String Description;

    @Enumerated(EnumType.STRING)
    @Column( name = "Status",nullable = false)
    private CreatorProfileStatus status;

    @Column(nullable = false,name = "AvatarURL")//
    private String AvatarURL;

    @Column(name = "Tiktok")
    private String Tiktok;

    @Column(name = "Instagram")
    private String Instagram;

    @Column(name = "Twitch")
    private String Twitch;

    @Column(name = "Youtube")
    private String Youtube;

    @Column(name = "Rating")
    private float rating;

    @Column(name="Subscribers_Count",nullable = false)//--
    private int SubscribersCount;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Post> posts;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Profile_has_tags> phs;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Complaint> complaints;

    public Profile(){}
}
