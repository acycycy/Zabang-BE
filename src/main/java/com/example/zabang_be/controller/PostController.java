package com.example.zabang_be.controller;

import com.example.zabang_be.dto.*;
import com.example.zabang_be.entity.PostEntity;
import com.example.zabang_be.repository.CommentRepository;
import com.example.zabang_be.repository.PostImageRepository;
import com.example.zabang_be.repository.PostRepository;
import com.example.zabang_be.repository.UserRepository;
import com.example.zabang_be.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RequestMapping("/api/community")

@RestController
@RequiredArgsConstructor
// @CrossOrigin(origins = "http://localhost:5173") // 프론트 도메인 포트
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostImageRepository postImageRepository;

    private static final DateTimeFormatter FE_DATE_FMT = DateTimeFormatter.ofPattern("yyyy.MM.dd");

//    @GetMapping("/test-posts")
//    public List<PostEntity> getPosts() {
//        return postRepository.findAll();
//    }

//    @GetMapping("/posts")
//    public Page<PostEntity> list(org.springframework.data.domain.Pageable pageable) {
//        return postRepository.findAll(pageable); // /posts?page=0&size=20&sort=id,desc
//    }


    private FrontendPostDto toFe(PostEntity post) {
        var comments = commentRepository
                .findViewsByPostId(post.getId(), Pageable.unpaged())
                .getContent()
                .stream()
                .map(c -> FrontendCommentDto.builder()
                        .id("c" + c.getId())
                        .author(c.getNickname())
                        .text(c.getContent())
                        .date(c.getCreatedAt().format(FE_DATE_FMT))
                        .build())
                .toList();

        String firstImage = postImageRepository
                .findUrlsByPostIdOrderByOrd(post.getId())
                .stream().findFirst().orElse(null);

        String author = userRepository.findById(post.getUserId())
                .map(u -> u.getNickName())
                .orElse("익명");

        return FrontendPostDto.builder()
                .id("p" + post.getId())
                .purchaseType(post.getCategory() == PostEntity.Category.GROUP_BUY)
                .purchaseStatus(post.getStatus() == PostEntity.Status.OPEN)
                .region(post.getAreaTag())
                .title(post.getTitle())
                .author(author)
                .date(post.getCreatedAt().format(FE_DATE_FMT))
                .contents(post.getContent())
                .image(firstImage)
                .views(post.getViewCount())
                .comments(comments)
                .build();
    }

    @PostMapping("/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public IdResponseDto create(@Valid @RequestBody PostCreateRequestDto req) {
        Long id = postService.create(req);
        return new IdResponseDto(id);
    }

    @GetMapping("/posts")
    public Page<PostListItemDto> list(
            @RequestParam(required = false) String category,  // FREE | GROUP_BUY | null
            @RequestParam(required = false) String areaTag,   // "1구역"~"4구역" | null
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return postService.listForFeed(category, areaTag, pageable);
    }

    @GetMapping("/fe/posts") //프론트 수정
    public List<FrontendPostDto> listForFrontend() {
        // 삭제 안 된 글만 가져오기
        var posts = postRepository
                .findAllByDeletedAtIsNull(org.springframework.data.domain.Pageable.unpaged())
                .getContent();

        return posts.stream().map(post -> {
            // 댓글 목록 가져오기
            var commentEntities = commentRepository.findViewsByPostId(post.getId(), Pageable.unpaged()).getContent();

            var comments = commentEntities.stream()
                    .map(c -> FrontendCommentDto.builder()
                            .id("c" + c.getId())
                            .author(c.getNickname())
                            .text(c.getContent())
                            .date(c.getCreatedAt().format(FE_DATE_FMT))
                            .build())
                    .toList();

            return FrontendPostDto.builder()
                    .id("p" + post.getId())
                    .purchaseType(post.getCategory() == PostEntity.Category.GROUP_BUY)
                    .purchaseStatus(post.getStatus() == PostEntity.Status.OPEN)
                    .region(post.getAreaTag())
                    .title(post.getTitle())
                    .author(userRepository.findById(post.getUserId())
                            .map(u -> u.getNickName())
                            .orElse("익명"))
                    .date(post.getCreatedAt().format(FE_DATE_FMT))
                    .contents(post.getContent())
                    .image(postImageRepository.findUrlsByPostIdOrderByOrd(post.getId()).stream().findFirst().orElse(null))
                    .views(post.getViewCount())
                    .comments(comments)
                    .build();
        }).toList();
    }


    @GetMapping("/fe/posts/{id}")  // 프론트 수정
    public FrontendPostDto getFrontendPost(@PathVariable Long id) {
        PostEntity post = postRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        return toFe(post);
    }



    @GetMapping("/posts/{id}")
    public PostDetailDto getDetail(@PathVariable Long id) {
        return postService.getDetail(id);
    }


//    @PatchMapping("/posts/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void update(@PathVariable Long id, @RequestBody PostCreateRequestDto req) {
//        postService.update(id, req);
//    }

    // PostController
    @PatchMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @Valid @RequestBody PostUpdateRequestDto req) { // ← 타입 교체
        postService.update(id, req);
    }


    @DeleteMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        postService.delete(id);
    }


    // 검색 API
    @GetMapping("/posts/search")
    public Page<PostListItemDto> searchPosts(
            @RequestParam("query") String query,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String areaTag,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return postService.searchForFeed(query, category, areaTag, pageable);
    }


}
