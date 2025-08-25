package com.example.zabang_be.repository;

import com.example.zabang_be.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    // 상세 조회용 (삭제 안 된 글만)
    Optional<PostEntity> findByIdAndDeletedAtIsNull(Long id);
    // 목록 페이징 (삭제 안 된 글만)
    Page<PostEntity> findAllByDeletedAtIsNull(Pageable pageable);

    public interface PostListItemView {
        Long getId();
        String getTitle();
        String getCategory();     // ENUM → 문자열
        String getAreaTag();
        String getStatus();       // ENUM → 문자열
        java.time.LocalDateTime getCreatedAt();
        Integer getViewCount();
        Integer getCommentCount();
        String getThumbnail();
    }

    // PostRepository
    @Query(
            value = """
    SELECT
      p.id                           AS id,
      p.title                        AS title,
      p.category                     AS category,
      p.area_tag                     AS areaTag,
      p.status                       AS status,
      p.created_at                   AS createdAt,
      p.view_count                   AS viewCount,
      (SELECT COUNT(1)
         FROM comments c
        WHERE c.post_id = p.id AND c.is_deleted = 0) AS commentCount,
      (SELECT i.url
         FROM post_image i
        WHERE i.post_id = p.id
        ORDER BY i.ord ASC, i.id ASC
        LIMIT 1)                   AS thumbnail
    FROM posts p
    WHERE p.deleted_at IS NULL
    ORDER BY p.id DESC 
    """,
            countQuery = """
    SELECT COUNT(1)
    FROM posts p
    WHERE p.deleted_at IS NULL
    """,
            nativeQuery = true
    )
    Page<PostListItemView> findPostList(Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM comments c WHERE c.post_id = :postId AND c.is_deleted = 0",
            nativeQuery = true)
    int countCommentsByPostId(@Param("postId") Long postId);


    //검색
    @Query(
            value = """
  SELECT
    p.id                           AS id,
    p.title                        AS title,
    p.category                     AS category,
    p.area_tag                     AS areaTag,
    p.status                       AS status,
    p.created_at                   AS createdAt,
    p.view_count                   AS viewCount,
    (SELECT COUNT(1)
       FROM comments c
      WHERE c.post_id = p.id AND c.is_deleted = 0) AS commentCount,
    (SELECT i.url
       FROM post_image i
      WHERE i.post_id = p.id
      ORDER BY i.ord ASC, i.id ASC
      LIMIT 1)                    AS thumbnail
  FROM posts p
  WHERE p.deleted_at IS NULL
    AND (:category IS NULL OR p.category = :category)
    AND (:areaTag  IS NULL OR p.area_tag = :areaTag)
    AND (:q IS NULL OR (p.title LIKE CONCAT('%', :q, '%') OR p.content LIKE CONCAT('%', :q, '%')))
  ORDER BY p.id DESC
  """,
            countQuery = """
  SELECT COUNT(1)
  FROM posts p
  WHERE p.deleted_at IS NULL
    AND (:category IS NULL OR p.category = :category)
    AND (:areaTag  IS NULL OR p.area_tag = :areaTag)
    AND (:q IS NULL OR (p.title LIKE CONCAT('%', :q, '%') OR p.content LIKE CONCAT('%', :q, '%')))
  """,
            nativeQuery = true
    )
    Page<PostListItemView> searchPostList(
            @Param("category") String category,
            @Param("areaTag") String areaTag,
            @Param("q") String q,
            Pageable pageable
    );


}