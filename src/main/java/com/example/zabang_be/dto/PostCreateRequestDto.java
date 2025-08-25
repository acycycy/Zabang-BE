package com.example.zabang_be.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateRequestDto {
    @NotNull(message = "userId is required")
    private Long userId;

    @NotBlank(message = "title is required")
    @Size(max = 100, message = "title max 100")
    private String title;

    @NotBlank(message = "content is required")
    @Size(max = 5000, message = "content max 5000")
    private String content;

    @NotBlank(message = "status is required")
    @Size (max = 5000, message = "content max 5000")
    private String status;

    @NotBlank(message = "areaTag is required")  // "1구역" ~ "4구역"
    private String areaTag;

    @NotBlank(message = "category is required") // FREE or GROUP_BUY
    private String category;

    private java.util.List<@NotBlank(message = "image url cannot be blank") String> imageUrls;

}
