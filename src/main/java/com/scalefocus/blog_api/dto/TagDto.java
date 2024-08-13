
package com.scalefocus.blog_api.dto;

import lombok.Builder;

@Builder
public record TagDto(
        Long id,
        String name
        ) {}

