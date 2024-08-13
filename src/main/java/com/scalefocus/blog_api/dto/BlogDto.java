package com.scalefocus.blog_api.dto;


import lombok.Builder;

import java.util.Set;

@Builder
public record BlogDto(
        Long id,
        String title,
        String text,
        Set<TagDto> tagDtoSet

) {}
