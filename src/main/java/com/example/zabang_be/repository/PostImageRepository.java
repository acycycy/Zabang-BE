package com.example.zabang_be.repository;

import com.example.zabang_be.entity.PostImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImageEntity, Long> {
    //엔티티 그대로 조회 (정렬 최신순)
    List<PostImageEntity> findByPostIdOrderByOrdAscIdAsc(Long postId);

    @Query("select i.url from PostImageEntity i where i.postId = :postId order by i.ord asc, i.id asc")
    List<String> findUrlsByPostIdOrderByOrd(@Param("postId") Long postId);
}
