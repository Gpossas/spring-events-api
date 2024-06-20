package com.example.api.domain.events;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

public record EventResponseDTO(
        UUID id,
        String title,
        String description,
        Date date,
        String city,
        String uf,
        Boolean is_remote,
        String event_url,
        String image_url
) {}
