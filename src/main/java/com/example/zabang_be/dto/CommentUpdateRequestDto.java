package com.example.zabang_be.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CommentUpdateRequestDto {
    @NotBlank
    @Size(max = 1000)
    private String content;
}
