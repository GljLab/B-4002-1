package com.label4002.blog.dto;

import java.time.LocalDateTime;

public record PostDetailDTO(
        Long id,
        String title,
        String content,
        String authorName,
        LocalDateTime createdAt
) {
}
