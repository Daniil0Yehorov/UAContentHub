package com.UCH.UAContentHub.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter//ПРАЦЮЄ МБ ДЛЯ ІНШИХ СУТНОСТЕЙ ТАКОЖ ЗРОБИТИ
@Table(name = "Post_has_Image", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"Image_id"})
})
public class Post_has_Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private int id;

    @Column(name = "sequence",nullable = false)
    private int sequence;
    @ManyToOne
    @JoinColumn(name = "Post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "Image_id", nullable = false)
    private Image image;

    public Post_has_Image (){}
}
