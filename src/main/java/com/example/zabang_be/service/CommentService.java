package com.example.zabang_be.service;

import com.example.zabang_be.dto.*;
import com.example.zabang_be.entity.CommentEntity;
import com.example.zabang_be.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

    @Transactional
    public Long create(Long postId, CommentCreateRequestDto req) {
        if (req.getUserId() == null) throw new IllegalArgumentException("userId required");
        if (req.getContent() == null || req.getContent().isBlank()) throw new IllegalArgumentException("content required");

        CommentEntity c = CommentEntity.builder()
                .postId(postId)
                .userId(req.getUserId())
                .content(req.getContent())
                .isDeleted(false)
                .build();
        return commentRepository.save(c).getId();
    }

    @Transactional(readOnly = true)
    public Page<CommentResponseDto> list(Long postId, Pageable pageable) {
        Page<CommentRepository.CommentView> page = commentRepository.findViewsByPostId(postId, pageable);
        return page.map(v -> CommentResponseDto.builder()
                .id(v.getId())
                .postId(v.getPostId())
                .userId(v.getUserId())
                .nickname(v.getNickname())
                .content(v.getContent())
                .createdAt(v.getCreatedAt().format(FMT))
                .build());
    }

    @Transactional
    public void update(Long commentId, CommentUpdateRequestDto req) {
        CommentEntity c = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        if (c.isDeleted()) throw new IllegalStateException("Comment already deleted");
        if (req.getContent() != null && !req.getContent().isBlank()) {
            c.setContent(req.getContent());
        }
    }

    @Transactional
    public void delete(Long commentId) {
        CommentEntity c = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        if (!c.isDeleted()) c.setDeleted(true); // soft delete
    }

    @Transactional(readOnly = true)
    public int countAliveByPostId(Long postId) {
        return commentRepository.countAliveByPostId(postId);
    }
}
