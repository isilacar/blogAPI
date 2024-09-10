package com.scalefocus.blog_api.service.impl;


import com.scalefocus.blog_api.dto.BlogDto;
import com.scalefocus.blog_api.dto.TagDto;
import com.scalefocus.blog_api.entity.Blog;
import com.scalefocus.blog_api.entity.Tag;
import com.scalefocus.blog_api.entity.User;
import com.scalefocus.blog_api.exception.ResourceNotFound;
import com.scalefocus.blog_api.mapper.BlogMapper;
import com.scalefocus.blog_api.repository.BlogRepository;
import com.scalefocus.blog_api.repository.TagRepository;
import com.scalefocus.blog_api.repository.UserRepository;
import com.scalefocus.blog_api.request.BlogCreationRequest;
import com.scalefocus.blog_api.request.BlogUpdateRequest;
import com.scalefocus.blog_api.request.TagAddRequest;
import com.scalefocus.blog_api.response.*;
import com.scalefocus.blog_api.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final BlogMapper blogMapper;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    @Override
    public BlogDto createBlog(BlogCreationRequest blogCreationRequest) {

        User user = userRepository.findById(blogCreationRequest.getUserId()).orElseThrow(() -> new ResourceNotFound("User Not Found"));
        Blog blog = blogMapper.getBlog(blogCreationRequest, user);
        Blog savedBlog = blogRepository.save(blog);
        return blogMapper.mapToBlogDto(savedBlog);
    }

    @Override
    public List<BlogDto> getAllBlogs() {
        return blogMapper.mapToBlogDtoList(blogRepository.findAll());
    }

    @Override
    public BlogDto updateBlog(Long blogId, BlogUpdateRequest blogUpdateRequest) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new ResourceNotFound("Blog does not exist with id: " + blogId));

        blog.setTitle(blogUpdateRequest.title());
        blog.setText(blogUpdateRequest.text());

        blogRepository.save(blog);

        return blogMapper.mapToBlogDto(blog);
    }

    @Override
    public BlogDto addTag(Long blogId, TagAddRequest tagAddRequest) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new ResourceNotFound("Blog does not exist with id: " + blogId));
        Tag tag = Tag.builder().name(tagAddRequest.tagName()).build();
        blog.getTags().add(tag);
        blogRepository.save(blog);

        return blogMapper.mapToBlogDto(blog);
    }

    @Override
    public BlogDto removeTag(Long blogId, Long tagId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new ResourceNotFound("Blog does not exist with id: " + blogId));
        Tag tag = blog.getTags().stream()
                .filter(t -> t.getId().equals(tagId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFound("Blog with id:" + blogId + " does not have any tag with id: " + tagId));

        blog.getTags().remove(tag);
        blogRepository.save(blog);
        tagRepository.deleteById(tagId);

        return blogMapper.mapToBlogDto(blog);
    }

    @Override
    public List<BlogDto> getBlogsByTagName(String tagName) {
        List<Blog> blogs = blogRepository.findByTagsName(tagName);

        return blogMapper.mapToBlogDtoList(blogs);
    }

    @Override
    public List<SimplifiedBlogResponse> getSimplifiedBlogs() {
        List<Blog> blogs = blogRepository.findAll();
        return blogs.stream().map(blog -> new SimplifiedBlogResponse(blog.getTitle(), blog.getText())).toList();
    }

    @Override
    public UserResponse deleteUserBlog(Long blogId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFound("User Not Found"));
        Blog blog = user.getBlogList().stream()
                .filter(b -> b.getId().equals(blogId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFound("Blog does not exist with id: " + blogId));
        user.getBlogList().remove(blog);
        User savedUser = userRepository.save(user);
        blogRepository.delete(blog);
        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(savedUser.getId());
        Set<TagDto> tagDtoSet = blog.getTags().stream().map(t -> new TagDto(t.getId(), t.getName())).collect(Collectors.toSet());
        userResponse.setBlogList(user.getBlogList().stream()
                .map(b -> new BlogDto(b.getId(), b.getTitle(), b.getText(), tagDtoSet))
                .collect(Collectors.toList()));
        return userResponse;

    }
    @Override
    public List<UserBlogResponse> getUsersBlogs() {

        return userRepository.findAll().stream().map(user -> {
            List<BlogResponse> blogResponses = user.getBlogList().stream()
                    .map(blog -> new BlogResponse(blog.getTitle(), blog.getText())).toList();
            UserBlogResponse userBlogResponse = new UserBlogResponse();
            userBlogResponse.setDisplayName(user.getDisplayName());
            userBlogResponse.setBlogs(blogResponses);

            return userBlogResponse;
        }).collect(Collectors.toList());

    }

}
