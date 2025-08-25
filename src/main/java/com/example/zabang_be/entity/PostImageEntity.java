package com.example.zabang_be.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter @NoArgsConstructor
@Entity @Table(name="post_image")
public class PostImageEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="post_id", nullable=false)
    private Long postId;

    @Column(nullable=false, length=500)
    private String url;

    @Column(nullable=false)
    private int ord;

    public PostImageEntity(Long postId, String url, int ord) {
        this.postId = postId;
        this.url = url;
        this.ord = ord;
    }
}
