package com.scalefocus.blog_api.service;


import com.scalefocus.blog_api.dto.BlogDto;
import com.scalefocus.blog_api.request.BlogCreationRequest;
import com.scalefocus.blog_api.request.BlogUpdateRequest;
import com.scalefocus.blog_api.request.TagAddRequest;
import com.scalefocus.blog_api.response.SimplifiedBlogResponsePagination;
import com.scalefocus.blog_api.response.UserBlogResponse;

import java.util.List;

public interface BlogService {

    BlogDto createBlog(BlogCreationRequest blogCreationRequest);

    List<BlogDto> getAllBlogs();

    UserBlogResponse getUserBlogs(String username);

    BlogDto updateBlog(Long blogId, BlogUpdateRequest blogUpdateRequest);

    BlogDto addTag(Long blogId, TagAddRequest tagAddRequest);

    BlogDto removeTag(Long blogId, Long tagId);

    List<BlogDto> getBlogsByTagName(String tagName);

    SimplifiedBlogResponsePagination getSimplifiedBlogs(int pageNumber, int pageSize);

    void deleteUserBlogByUsername(Long blogId, String username);
}
