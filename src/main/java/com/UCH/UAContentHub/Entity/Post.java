package com.UCH.UAContentHub.Entity;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "profile_UserID", referencedColumnName = "UserID", nullable = false)
    private Profile profile;

    @Column(nullable = false, length =65535)
    private String content;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false,name = "Publish_Date")
    private LocalDateTime publishDate;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likes> likes;

    @OneToMany(mappedBy = "post",orphanRemoval = true)
    private List<Complaint> complaints;

    @Column(nullable = false,name = "Like_Count")
    private int likeCount;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sequence ASC")
    private List<Post_has_Image> phi;

    public Post(){}

}
