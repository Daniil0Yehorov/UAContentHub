package com.UCH.UAContentHub.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class Tags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,name = "id")
    private int id;

    @OneToMany(mappedBy = "tags", orphanRemoval = true)
    private Set<Profile_has_tags> phs;

    @Column(unique = true, nullable = false,name="name")
    private String name;

    public Tags(int id, String name, Set<Profile_has_tags> phs) {
        this.id = id;
        this.name = name;
        this.phs = phs;
    }

    public Tags(){}
}