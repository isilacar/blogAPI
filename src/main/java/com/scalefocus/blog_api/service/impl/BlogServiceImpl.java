package com.scalefocus.blog_api.service.impl;


import com.scalefocus.blog_api.dto.BlogDto;
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
import com.scalefocus.blog_api.response.BlogResponse;
import com.scalefocus.blog_api.response.SimplifiedBlogResponse;
import com.scalefocus.blog_api.response.SimplifiedBlogResponsePagination;
import com.scalefocus.blog_api.response.UserBlogResponse;
import com.scalefocus.blog_api.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private static final Logger logger = LogManager.getLogger(BlogServiceImpl.class);

    private final BlogRepository blogRepository;
    private final BlogMapper blogMapper;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    @Override
    public BlogDto createBlog(BlogCreationRequest blogCreationRequest) {

        User user = userRepository.findById(blogCreationRequest.getUserId()).orElseThrow(() -> {
            logger.error("User not found with id '{}'", blogCreationRequest.getUserId());
            return new ResourceNotFound("User not found with id: " + blogCreationRequest.getUserId());
        });

        logger.info("User with id '{}' has founded", blogCreationRequest.getUserId());
        Blog blog = blogMapper.getBlog(blogCreationRequest, user);
        Blog savedBlog = blogRepository.save(blog);
        logger.info("Blog has created successfully by the user id '{}'", user.getId());
        return blogMapper.mapToBlogDto(savedBlog);
    }

    @Override
    public List<BlogDto> getAllBlogs() {
        logger.info("Getting all blogs from the database");
        return blogMapper.mapToBlogDtoList(blogRepository.findAll());
    }

    @Override
    public BlogDto updateBlog(Long blogId, BlogUpdateRequest blogUpdateRequest) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> {
                    logger.error("Blog does not exist with id '{}'", blogId);
                    return new ResourceNotFound("Blog does not exist with id: " + blogId);
                });
        logger.info("Blog has found with id '{}'", blogId);
        blog.setTitle(blogUpdateRequest.title());
        blog.setText(blogUpdateRequest.text());

        blogRepository.save(blog);
        logger.info("Blog with id '{}' has updated successfully", blogId);
        return blogMapper.mapToBlogDto(blog);
    }

    @Override
    public BlogDto addTag(Long blogId, TagAddRequest tagAddRequest) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> {
                    logger.error("Blog does not found with id '{}'", blogId);
                    return new ResourceNotFound("Blog does not exist with id: " + blogId);
                });
        logger.info("Blog has found with id '{}'", blogId);
        Tag tag = Tag.builder().name(tagAddRequest.tagName()).build();
        blog.getTags().add(tag);
        blogRepository.save(blog);
        logger.info("New tag has added successfully to the blog with id '{}'", blogId);
        return blogMapper.mapToBlogDto(blog);
    }

    @Override
    public BlogDto removeTag(Long blogId, Long tagId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> {
                    logger.error("Blog does not exist with id '{}'", blogId);
                    return new ResourceNotFound("Blog does not exist with id: " + blogId);
                });
        logger.info("Blog with id '{}' has found", blogId);
        Tag tag = blog.getTags().stream()
                .filter(t -> t.getId().equals(tagId))
                .findFirst()
                .orElseThrow(() -> {
                    logger.error("Blog with id '{}' does not have any tag with id '{}'", blogId, tagId);
                    return new ResourceNotFound("Blog with id:" + blogId + " does not have any tag with id: " + tagId);
                });
        logger.info("Tag with id '{}' has found", tagId);

        blog.getTags().remove(tag);
        blogRepository.save(blog);
        tagRepository.deleteById(tagId);

        logger.info("Tag with id '{}' has removed successfully from the blog with id '{}'", tagId, blogId);
        return blogMapper.mapToBlogDto(blog);
    }

    @Override
    public List<BlogDto> getBlogsByTagName(String tagName) {
        List<Blog> blogs = blogRepository.findByTagsName(tagName);
        logger.info("Getting all blogs with specified tag name '{}'", tagName);
        return blogMapper.mapToBlogDtoList(blogs);
    }


    @Override
    public SimplifiedBlogResponsePagination getSimplifiedBlogs(int pageNumber, int pageSize) {
        Page<Blog> blogRepositoryPagination = blogRepository.findAll(PageRequest.of(pageNumber, pageSize));
        logger.info("Getting all simplified blogs with pagination");

        SimplifiedBlogResponsePagination responsePagination = new SimplifiedBlogResponsePagination();
        responsePagination.setSimplifiedBlogResponseList(blogRepositoryPagination.getContent().stream().map(data -> new SimplifiedBlogResponse(data.getTitle(), data.getText())).toList());
        responsePagination.setTotalValue(blogRepositoryPagination.getTotalElements());
        responsePagination.setTotalPages(blogRepositoryPagination.getTotalPages());
        responsePagination.setCurrentPage(blogRepositoryPagination.getPageable().getPageNumber());
        responsePagination.setViewedValueCount(blogRepositoryPagination.getPageable().getPageSize());

        return responsePagination;
    }

    @Override
    public void deleteUserBlogByUsername(Long blogId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User with username '{}' does not exist", username);
                    return new ResourceNotFound("User not found with name: " + username);
                });

        logger.info("User has found with user id '{}'", user.getId());
        Blog blog = user.getBlogList().stream().filter(b -> b.getId().equals(blogId)).findFirst()
                .orElseThrow(() -> {
                    logger.error("Blog does not found with id '{}'", blogId);
                    return new ResourceNotFound("Blog does not exist with id: " + blogId);
                });
        logger.info("Blog has found with id '{}'", blogId);
        user.getBlogList().remove(blog);
        userRepository.save(user);
        blogRepository.delete(blog);
        logger.info("Blog with id '{}' has deleted successfully which belongs to user with id '{}'", blogId, user.getId());
    }

    public UserBlogResponse getUserBlogs(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            logger.error("User with username '{}' does not exist", username);
            return new ResourceNotFound("User Not Found with name: " + username);
        });
        logger.info("User has found with user id '{}'", user.getId());
        List<Blog> userBlogs = blogRepository.getBlogsByUserUsername(username);
        List<BlogResponse> blogResponseList = userBlogs.stream().map(blog -> new BlogResponse(blog.getTitle(), blog.getText())).toList();

        UserBlogResponse userBlogResponse = new UserBlogResponse();
        userBlogResponse.setDisplayName(user.getDisplayName());
        userBlogResponse.setBlogs(blogResponseList);
        logger.info("Getting all user blogs which are belongs to user id '{}'", user.getId());
        return userBlogResponse;
    }

}
