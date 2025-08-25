package com.example.zabang_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class FrontendPostDto {
    private String id;              // "p1"
    private boolean purchaseType;   // 공동구매 여부
    private boolean purchaseStatus; // 진행중 여부
    private String region;          // 구역
    private String title;
    private String author;          // 닉네임
    private String date;            // yyyy.MM.dd
    private String contents;
    private String image;
    private int views;
    private List<FrontendCommentDto> comments;
}
