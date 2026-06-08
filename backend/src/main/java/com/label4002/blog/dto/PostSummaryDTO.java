package com.label4002.blog.dto;

import java.time.LocalDateTime;

public record PostSummaryDTO(
        Long id,
        String title,
        String excerpt,
        String authorName,
        LocalDateTime createdAt
) {
}
