package com.example.zabang_be.dto;

import com.example.zabang_be.entity.PostEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostListItemDto {
    private Long id;
    private String title;
    private String category;
    private String areaTag;
    private String status;
    private String createdAt;
    private int viewCount;
    private int commentCount;
    private String thumbnail;

    // Entity → DTO 변환
    public static PostListItemDto fromEntity(PostEntity post, int commentCount, String thumbnailUrl, String formattedDate) {
        return PostListItemDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .category(post.getCategory().name())
                .areaTag(post.getAreaTag())
                .status(post.getStatus().name())
                .createdAt(formattedDate)
                .viewCount(post.getViewCount())
                .commentCount(commentCount)
                .thumbnail(thumbnailUrl)
                .build();
    }
}
