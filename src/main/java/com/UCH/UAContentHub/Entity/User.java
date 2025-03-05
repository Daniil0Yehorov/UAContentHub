package com.UCH.UAContentHub.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import com.UCH.UAContentHub.Entity.Enum.Role;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "User", uniqueConstraints = {
        @UniqueConstraint(name = "Email", columnNames = "Email"),
        @UniqueConstraint(name = "login_UNIQUE", columnNames = "login")
})
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,name = "id")
    private int id;

    @Column(unique = true, nullable = false,name = "login", length = 64)
    private String login;

    @Column(nullable = false,name = "Password", length = 64)
    private String Password;

    @Column(unique = true, nullable = false,name = "Name", length = 64)
    private String Name;

    @Column(unique = true, nullable = false,name = "Email")
    private String Email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,name = "Role")
    private Role role;

    @Column(nullable = false,name = "Registration_Date")
    private LocalDateTime RegistrationDate;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Complaint> complaints;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subscription> subscriptions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likes> likes;

    public User() {}
}
