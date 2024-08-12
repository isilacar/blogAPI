package com.scalefocus.blog_api.dto;


import java.util.Set;

public record BlogDto(
        Long id,
        String title,
        String text,
        Set<TagDto> tagDtoSet
) {}
