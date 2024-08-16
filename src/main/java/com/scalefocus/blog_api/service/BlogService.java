package com.scalefocus.blog_api.service;


import com.scalefocus.blog_api.dto.BlogDto;
import com.scalefocus.blog_api.request.BlogUpdateRequest;

import java.util.List;

public interface BlogService {

    BlogDto createBlog(BlogDto blogDto);

    List<BlogDto> getAllBlogs();

    BlogDto updateBlog(Long blogId, BlogUpdateRequest blogUpdateRequest);
}
