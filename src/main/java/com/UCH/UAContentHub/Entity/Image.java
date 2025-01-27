package com.UCH.UAContentHub.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;


@Entity
@Getter
@Setter
@Table(name = "Image", uniqueConstraints = {
        @UniqueConstraint(name = "Src_UNIQUE", columnNames = "Src")
})
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private int id;

    @Column(name = "Src",nullable = false)
    private String Src;

    @OneToMany(mappedBy = "image", orphanRemoval = true)
    private List<Post_has_Image> phi;

    public Image(){}
}
