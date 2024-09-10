package com.scalefocus.blog_api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBlogResponse {
    private String displayName;
    private List<BlogResponse> blogs;
}