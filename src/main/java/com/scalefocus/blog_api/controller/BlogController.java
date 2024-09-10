package com.scalefocus.blog_api.controller;


import com.scalefocus.blog_api.dto.BlogDto;
import com.scalefocus.blog_api.request.BlogCreationRequest;
import com.scalefocus.blog_api.request.BlogUpdateRequest;
import com.scalefocus.blog_api.request.TagAddRequest;
import com.scalefocus.blog_api.response.SimplifiedBlogResponse;
import com.scalefocus.blog_api.response.UserBlogResponse;
import com.scalefocus.blog_api.response.UserResponse;
import com.scalefocus.blog_api.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    //authenticated users can create blogs
    @PostMapping
    public ResponseEntity<BlogDto> createBlog(@RequestBody BlogCreationRequest blogCreationRequest) {
        return new ResponseEntity<>(blogService.createBlog(blogCreationRequest), HttpStatus.CREATED);
    }

    //users can get all the blogs
    @GetMapping
    public ResponseEntity<List<BlogDto>> getAllBlogs() {
        return new ResponseEntity<>(blogService.getAllBlogs(), HttpStatus.OK);
    }

    //authenticated users can get other user's posts
    @GetMapping("/users")
    public ResponseEntity<List<UserBlogResponse>> getUserBlogs() {
        return new ResponseEntity<>(blogService.getUsersBlogs(), HttpStatus.OK);
    }

    //users can update specific blog
    @PutMapping("/{blogId}")
    public ResponseEntity<BlogDto> updateBlog(@PathVariable Long blogId,
                                              @RequestBody BlogUpdateRequest blogUpdateRequest) {
        return new ResponseEntity<>(blogService.updateBlog(blogId, blogUpdateRequest), HttpStatus.OK);
    }

    //users can add new tag to existing blogs
    @PutMapping("/{blogId}/tags")
    public ResponseEntity<BlogDto> addTagToBlog(@PathVariable Long blogId, @RequestBody TagAddRequest tagAddRequest) {
        return new ResponseEntity<>(blogService.addTag(blogId, tagAddRequest), HttpStatus.OK);
    }

    //users can remove any tag from specific blog
    @DeleteMapping("/{blogId}/tags/{tagId}")
    public ResponseEntity<BlogDto> deleteTagFromBlog(@PathVariable Long blogId, @PathVariable Long tagId) {
        return new ResponseEntity<>(blogService.removeTag(blogId, tagId), HttpStatus.OK);
    }

    //users can get all blogs by specific tagName
    @GetMapping("/tagName/{tagName}")
    public ResponseEntity<List<BlogDto>> getAllBlogsByTagName(@PathVariable String tagName) {
        return new ResponseEntity<>(blogService.getBlogsByTagName(tagName), HttpStatus.OK);
    }

    //users can get simplified blog list
    @GetMapping("/simplified")
    public ResponseEntity<List<SimplifiedBlogResponse>> getSimplifiedBlogs() {
        return new ResponseEntity<>(blogService.getSimplifiedBlogs(), HttpStatus.OK);
    }

    //authenticated users can remove own blogs
    @DeleteMapping("/{blogId}/users/{userId}")
    public ResponseEntity<UserResponse> deleteUserBlog(@PathVariable Long blogId, @PathVariable Long userId) {
        return new ResponseEntity<>(blogService.deleteUserBlog(blogId, userId), HttpStatus.OK);
    }
}


