package com.example.zabang_be.service;

import com.example.zabang_be.dto.PostCreateRequestDto;
import com.example.zabang_be.dto.PostDetailDto;
import com.example.zabang_be.dto.PostListItemDto;
import com.example.zabang_be.dto.PostUpdateRequestDto;
import com.example.zabang_be.entity.PostEntity;
import com.example.zabang_be.repository.PostRepository;
import com.example.zabang_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.zabang_be.entity.PostImageEntity;
import com.example.zabang_be.repository.PostImageRepository;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final UserRepository userRepository;
    private final CommentService commentService;

    private static final DateTimeFormatter FEED_DATE_FMT = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    @Transactional(readOnly = true)
    public Page<PostEntity> list(Pageable pageable) {
        return postRepository.findAllByDeletedAtIsNull(pageable);
    }


    @Transactional
    public Long create(PostCreateRequestDto req) {
        // 1) 기본 검증
        if (req.getUserId() == null) throw new IllegalArgumentException("userId required");
        if (req.getTitle() == null || req.getTitle().isBlank()) throw new IllegalArgumentException("title required");
        if (req.getContent() == null || req.getContent().isBlank()) throw new IllegalArgumentException("content required");
        if (req.getAreaTag() == null || req.getAreaTag().isBlank()) throw new IllegalArgumentException("areaTag required");
        if (req.getCategory() == null || req.getCategory().isBlank()) throw new IllegalArgumentException("category required");
        if(req.getStatus() == null || req.getStatus() == null) throw new IllegalArgumentException("status required");

        // 2) DTO → Entity
        PostEntity p = new PostEntity();
        p.setUserId(req.getUserId());
        p.setTitle(req.getTitle());
        p.setContent(req.getContent());
        p.setAreaTag(req.getAreaTag());
        p.setCategory(PostEntity.Category.valueOf(req.getCategory().toUpperCase()));
        p.setStatus(PostEntity.Status.OPEN);
        p.setViewCount(0);
        p.setDeletedAt(null);

        // 3) 게시글 저장 (한 번만!)
        Long postId = postRepository.save(p).getId();

        // 4) 이미지 저장
        if (req.getImageUrls() != null && !req.getImageUrls().isEmpty()) {
            int ord = 0;
            for (String url : req.getImageUrls()) {
                if (url == null || url.isBlank()) continue;
                postImageRepository.save(new PostImageEntity(postId, url, ord++));
            }
        }

        // 5) id 반환
        return postId;
    }


    @Transactional
    public PostDetailDto getDetail(Long id) {
        var post = postRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        // 조회수 +1
        post.setViewCount(post.getViewCount() + 1);

        // 이미지 URL들
        var urls = postImageRepository.findUrlsByPostIdOrderByOrd(post.getId());

        // 작성자 닉네임 (users 테이블)
        String nickname = userRepository.findById(post.getUserId())
                .map(com.example.zabang_be.entity.UserEntity::getNickName)
                .orElse(null);

        // 댓글 수 (삭제 제외)
        int commentCount = commentService.countAliveByPostId(post.getId());


        return PostDetailDto.builder()
                .id(post.getId())
                .userId(post.getUserId())
                .authorNickname(nickname)
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory().name())
                .areaTag(post.getAreaTag())
                .status(post.getStatus().name())
                .createdAt(post.getCreatedAt().format(FEED_DATE_FMT))
                .viewCount(post.getViewCount())
                .commentCount(commentCount)
                .images(urls)
                .build();
    }




    @Transactional
    public void update(Long id, PostUpdateRequestDto req) {
        PostEntity post = postRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (req.getTitle()   != null) post.setTitle(req.getTitle());
        if (req.getContent() != null) post.setContent(req.getContent());
        if (req.getAreaTag() != null) post.setAreaTag(req.getAreaTag());
        if (req.getStatus() != null) {
            try {
                post.setStatus(PostEntity.Status.valueOf(req.getStatus().toUpperCase())); // OPEN / CLOSED
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("status must be OPEN or CLOSED");
            }
        }
    }

    @Transactional
    public void delete(Long id) {
        PostEntity post = postRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        post.setDeletedAt(java.time.LocalDateTime.now()); // soft delete
    }


    // PostService.java
    @Transactional(readOnly = true)
    public Page<PostListItemDto> listForFeed(String category, String areaTag, Pageable pageable) {
        // category는 ENUM이므로 대문자 정규화 + 유효성 체크(빈 문자열은 null 처리)
        String cat = (category == null || category.isBlank()) ? null : category.trim().toUpperCase();
        if (cat != null && !cat.equals("FREE") && !cat.equals("GROUP_BUY")) {
            throw new IllegalArgumentException("category must be FREE or GROUP_BUY");
        }
        String area = (areaTag == null || areaTag.isBlank()) ? null : areaTag.trim(); // "1구역"~"4구역"

        Page<PostRepository.PostListItemView> page = postRepository.searchPostList(cat, area, null, pageable);

        return page.map(v -> PostListItemDto.builder()
                .id(v.getId())
                .title(v.getTitle())
                .category(v.getCategory())
                .areaTag(v.getAreaTag())
                .status(v.getStatus())
                .createdAt(v.getCreatedAt().format(FEED_DATE_FMT))
                .viewCount(v.getViewCount() == null ? 0 : v.getViewCount())
                .commentCount(v.getCommentCount() == null ? 0 : v.getCommentCount())
                .thumbnail(v.getThumbnail() == null ? "" : v.getThumbnail())
                .build());
    }

    // 검색
    @Transactional(readOnly = true)
    public Page<PostListItemDto> searchForFeed(String query, String category, String areaTag, Pageable pageable) {
        String cat = (category == null || category.isBlank()) ? null : category.trim().toUpperCase();
        if (cat != null && !cat.equals("FREE") && !cat.equals("GROUP_BUY")) {
            throw new IllegalArgumentException("category must be FREE or GROUP_BUY");
        }
        String area = (areaTag == null || areaTag.isBlank()) ? null : areaTag.trim();
        String q = (query == null || query.isBlank()) ? null : query.trim();

        Page<PostRepository.PostListItemView> page = postRepository.searchPostList(cat, area, q, pageable);

        return page.map(v -> PostListItemDto.builder()
                .id(v.getId())
                .title(v.getTitle())
                .category(v.getCategory())
                .areaTag(v.getAreaTag())
                .status(v.getStatus())
                .createdAt(v.getCreatedAt().format(FEED_DATE_FMT))
                .viewCount(v.getViewCount() == null ? 0 : v.getViewCount())
                .commentCount(v.getCommentCount() == null ? 0 : v.getCommentCount())
                .thumbnail(v.getThumbnail() == null ? "" : v.getThumbnail())
                .build());
    }








}
