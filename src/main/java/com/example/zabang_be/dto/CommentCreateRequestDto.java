package com.example.zabang_be.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CommentCreateRequestDto {
    @NotNull private Long userId;
    @NotBlank @Size(max = 1000) private String content;
}



