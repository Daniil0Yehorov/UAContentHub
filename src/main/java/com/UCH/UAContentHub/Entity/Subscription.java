package com.UCH.UAContentHub.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "UserID", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "CreatorID", referencedColumnName = "id", nullable = false)
    private User creator;

    @Column(nullable = false,name="Subscription_Date")
    private LocalDateTime SubscriptionDate;

    public Subscription(int id, User user, User creator, LocalDateTime subscriptionDate) {
        this.id = id;
        this.user = user;
        this.creator = creator;
        this.SubscriptionDate = subscriptionDate;
    }

    public Subscription(){}
}