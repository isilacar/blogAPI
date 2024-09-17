package com.scalefocus.blog_api.request;

import com.scalefocus.blog_api.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogCreationRequest {
    String title;
    String text;
    Set<Tag> tags=new HashSet<>();
    private Long userId;
}