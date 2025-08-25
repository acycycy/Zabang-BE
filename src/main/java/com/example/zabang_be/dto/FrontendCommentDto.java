package com.example.zabang_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class FrontendCommentDto {
    private String id;      // "c1" 처럼
    private String author;  // 닉네임
    private String text;    // 댓글 내용
    private String date;    // yyyy.MM.dd
}
