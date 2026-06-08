package com.label4002.blog.service;

import com.label4002.blog.dto.CreatePostRequest;
import com.label4002.blog.dto.PageResponse;
import com.label4002.blog.dto.PostDetailDTO;
import com.label4002.blog.dto.PostSummaryDTO;
import com.label4002.blog.dto.UpdatePostRequest;
import com.label4002.blog.entity.PostEntity;
import com.label4002.blog.entity.UserEntity;
import com.label4002.blog.exception.ForbiddenException;
import com.label4002.blog.exception.NotFoundException;
import com.label4002.blog.repository.PostRepository;
import com.label4002.blog.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<PostSummaryDTO> listPublic(int page, int size) {
        int normalizedPage = Math.max(page, 1);
        int normalizedSize = Math.min(Math.max(size, 1), 50);
        Page<PostEntity> postPage = postRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(normalizedPage - 1, normalizedSize));

        List<PostSummaryDTO> items = postPage.getContent().stream()
                .map(this::toSummary)
                .toList();

        return new PageResponse<>(items, postPage.getTotalElements(), normalizedPage, normalizedSize);
    }

    @Transactional(readOnly = true)
    public PostDetailDTO getDetail(Long postId) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("文章不存在"));
        return toDetail(post);
    }

    @Transactional(readOnly = true)
    public List<PostSummaryDTO> listMine(Long userId) {
        return postRepository.findByAuthorIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toSummary)
                .toList();
    }

    @Transactional
    public PostDetailDTO createPost(Long userId, CreatePostRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("用户不存在"));

        PostEntity post = new PostEntity();
        post.setTitle(normalizeTitle(request.title()));
        post.setContent(request.content().trim());
        post.setAuthor(user);

        return toDetail(postRepository.save(post));
    }

    @Transactional
    public PostDetailDTO updatePost(Long userId, Long postId, UpdatePostRequest request) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("文章不存在"));

        if (!post.getAuthor().getId().equals(userId)) {
            throw new ForbiddenException("无权限编辑他人的文章");
        }

        post.setTitle(normalizeTitle(request.title()));
        post.setContent(request.content().trim());
        PostEntity saved = postRepository.save(post);
        return toDetail(saved);
    }

    @Transactional
    public void deletePost(Long userId, Long postId) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("文章不存在"));

        if (!post.getAuthor().getId().equals(userId)) {
            throw new ForbiddenException("无权限删除他人的文章");
        }

        postRepository.delete(post);
    }

    private String normalizeTitle(String title) {
        return title.trim().replaceAll("\\s+", " ");
    }

    private PostSummaryDTO toSummary(PostEntity post) {
        return new PostSummaryDTO(
                post.getId(),
                post.getTitle(),
                excerpt(post.getContent(), 100),
                post.getAuthor().getUsername(),
                post.getCreatedAt()
        );
    }

    private PostDetailDTO toDetail(PostEntity post) {
        return new PostDetailDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor().getUsername(),
                post.getCreatedAt()
        );
    }

    private String excerpt(String content, int length) {
        String normalized = content == null ? "" : content.trim().replaceAll("\\s+", " ");
        if (normalized.length() <= length) {
            return normalized;
        }
        return normalized.substring(0, length) + "...";
    }
}
