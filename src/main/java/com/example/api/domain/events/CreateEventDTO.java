package com.example.api.domain.events;

import org.springframework.web.multipart.MultipartFile;

public record CreateEventDTO(
        String title,
        String description,
        Long date,
        String city,
        String uf,
        Boolean is_remote,
        String event_url,
        MultipartFile image
) {}
