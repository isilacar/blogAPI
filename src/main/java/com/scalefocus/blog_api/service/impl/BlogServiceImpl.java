package com.scalefocus.blog_api.service.impl;


import com.scalefocus.blog_api.dto.BlogDto;
import com.scalefocus.blog_api.entity.Blog;
import com.scalefocus.blog_api.mapper.BlogMapper;
import com.scalefocus.blog_api.repository.BlogRepository;
import com.scalefocus.blog_api.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;

    @Override
    public BlogDto createBlog(BlogDto blogDto) {
        Blog savedBlog = blogRepository.save(BlogMapper.mapToBlog(blogDto));
        return BlogMapper.mapToBlogDto(savedBlog);
    }


}
