package com.scalefocus.blog_api.service.impl;


import com.scalefocus.blog_api.dto.BlogDto;
import com.scalefocus.blog_api.entity.Blog;
import com.scalefocus.blog_api.mapper.BlogMapper;
import com.scalefocus.blog_api.repository.BlogRepository;
import com.scalefocus.blog_api.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final BlogMapper blogMapper;

    @Override
    public BlogDto createBlog(BlogDto blogDto) {
        Blog savedBlog = blogRepository.save(blogMapper.mapToBlog(blogDto));
        return blogMapper.mapToBlogDto(savedBlog);
    }

    @Override
    public List<BlogDto> getAllBlogs() {
        return blogMapper.mapToBlogDtoList(blogRepository.findAll());
    }


}
