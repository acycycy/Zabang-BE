package com.example.zabang_be.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

// 상세페이지 한번에

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDetailDto {
    private Long id;
    private Long userId;
    private String authorNickname;
    private String title;
    private String content;
    private String category;
    private String areaTag;
    private String status;
    private String createdAt;
    private LocalDateTime updatedDate;
    private int viewCount;
    private int commentCount;

    private java.util.List<String> images;
    private List<String> imageUrls;
}

