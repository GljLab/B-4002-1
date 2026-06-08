package com.label4002.blog.repository;

import com.label4002.blog.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

    Page<PostEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<PostEntity> findByAuthorIdOrderByCreatedAtDesc(Long authorId);

    Optional<PostEntity> findByIdAndAuthorId(Long id, Long authorId);
}
