package com.example.zabang_be.repository;

import com.example.zabang_be.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    // 목록 + 닉네임까지 한 번에 (삭제 안 된 것만)
    interface CommentView {
        Long getId();
        Long getPostId();
        Long getUserId();
        String getNickname();     // users.nickname
        String getContent();
        java.time.LocalDateTime getCreatedAt();
    }

    @Query(value = """
    SELECT c.id            AS id,
           c.post_id       AS postId,
           c.user_id       AS userId,
           u.nickname      AS nickname,
           c.content       AS content,
           c.created_at    AS createdAt
      FROM comments c
      JOIN users u ON u.userid = c.user_id   
     WHERE c.post_id = :postId
       AND c.is_deleted = 0
     ORDER BY c.id ASC
    """,
            countQuery = """
    SELECT COUNT(1)
      FROM comments c
     WHERE c.post_id = :postId
       AND c.is_deleted = 0
    """,
            nativeQuery = true)

    Page<CommentView> findViewsByPostId(@Param("postId") Long postId, Pageable pageable);

    @Query(value = "SELECT COUNT(1) FROM comments c WHERE c.post_id=:postId AND c.is_deleted=0", nativeQuery = true)
    int countAliveByPostId(@Param("postId") Long postId);

    // 살아있는 댓글만
    @Query("select c from CommentEntity c where c.id = :id and c.isDeleted = false")
    Optional<CommentEntity> findAliveById(@Param("id") Long id);

    @Query("select c from CommentEntity c where c.id = :id and c.isDeleted = false")
    Optional<CommentView> findViewById(@Param("id") Long id);

}
