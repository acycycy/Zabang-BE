package com.example.zabang_be.controller;


import com.example.zabang_be.dto.*;
import com.example.zabang_be.repository.CommentRepository;
import com.example.zabang_be.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;  //정렬 옵션 쓸 때
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RequestMapping("/api/community")

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository commentRepository;
    private final CommentService commentService;
    private static final DateTimeFormatter FE_DATE_FMT = DateTimeFormatter.ofPattern("yyyy.MM.dd");


    private static FrontendCommentDto toFe(CommentRepository.CommentView v) {
        return FrontendCommentDto.builder()
                .id("c" + v.getId())
                .author(v.getNickname())
                .text(v.getContent())
                .date(v.getCreatedAt().format(FE_DATE_FMT))
                .build();
    }

    // 댓글작성
    @PostMapping("/posts/{postId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public IdResponseDto create(@PathVariable Long postId,
                                @Valid @RequestBody CommentCreateRequestDto req) {
        Long id = commentService.create(postId, req);
        return new IdResponseDto(id);
    }

    // 댓글 목록 (기본: 오래된→최신)
    @GetMapping("/posts/{postId}/comments")
    public Page<CommentResponseDto> list(
            @PathVariable Long postId,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return commentService.list(postId, pageable);
    }



//    // 프론트 수정
//    @GetMapping("/fe/posts/{postId}/comments")
//    public Page<FrontendCommentDto> listForFrontend(
//            @PathVariable Long postId,
//            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
//    ) {
//        return commentRepository.findViewsByPostId(postId, pageable)
//                .map(CommentController::toFe);
//    }



    @GetMapping("/fe/posts/{postId}/comments")
    public List<FrontendCommentDto> listCommentsForFrontend(@PathVariable Long postId) {
        var views = commentRepository.findViewsByPostId(postId, Pageable.unpaged()).getContent();
        return views.stream()
                .map(v -> FrontendCommentDto.builder()
                        .id("c" + v.getId())
                        .author(v.getNickname())
                        .text(v.getContent())
                        .date(v.getCreatedAt().toLocalDate().toString())
                        .build())
                .toList();
    }




    // 댓글 수정
    @PatchMapping("/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @Valid @RequestBody CommentUpdateRequestDto req) {
        commentService.update(id, req);
    }

    // 댓글 삭제(soft)
    @DeleteMapping("/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        commentService.delete(id);
    }
}

