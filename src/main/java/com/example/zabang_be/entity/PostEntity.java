package com.example.zabang_be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "posts")
public class PostEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;                      // 우선 Long으로 매핑(연관관계는 나중에)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Category category;                // FREE / GROUP_BUY

    @Column(nullable = false, length = 100)
    private String title;

    @Lob @Column(nullable = false)
    private String content;

    @Column(name = "area_tag", nullable = false, length = 10)
    private String areaTag;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Status status;                    // OPEN / CLOSED

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    public enum Category { FREE, GROUP_BUY }
    public enum Status { OPEN, CLOSED }

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)//생성일은 수정해도 안바뀌게
    @CreatedDate// 생성 시 시각 자동 기록
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

//    @Column(name="is_deleted", updatable = false)
//    @LastModifiedDate
//    private Boolean deletedDate;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


}
