package com.UCH.UAContentHub.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Entity
@Getter
@Setter//ПРАЦЮЄ МБ ДЛЯ ІНШИХ СУТНОСТЕЙ ТАКОЖ ЗРОБИТИ
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private int id;

    @Column(name = "Src",nullable = false)
    private String Src;

    @OneToMany(mappedBy = "image", orphanRemoval = true)
    private Set<Post_has_Image> phi;

    public Image(){}
}
