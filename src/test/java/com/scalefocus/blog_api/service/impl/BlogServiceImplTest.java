package com.scalefocus.blog_api.service.impl;

import com.scalefocus.blog_api.dto.BlogDto;
import com.scalefocus.blog_api.entity.Blog;
import com.scalefocus.blog_api.mapper.BlogMapper;
import com.scalefocus.blog_api.repository.BlogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;

public class BlogServiceImplTest {

    @Mock
    private BlogRepository blogRepository;

    @Mock
    private BlogMapper blogMapper;

    @InjectMocks
    private BlogServiceImpl blogServiceImpl;

    private Blog blog;
    private BlogDto blogDto;
    private List<Blog> blogList;
    private List<BlogDto> blogDtoList;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        PodamFactory podamFactory = new PodamFactoryImpl();

        blog = podamFactory.manufacturePojo(Blog.class);
        blogList = List.of(blog);

        blogDto = BlogDto.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .text(blog.getText())
                .build();
        blogDtoList = List.of(blogDto);

        doReturn(blog).when(blogMapper).mapToBlog(any(BlogDto.class));
        doReturn(blogDto).when(blogMapper).mapToBlogDto(any(Blog.class));
        doReturn(blogDtoList).when(blogMapper).mapToBlogDtoList(anyList());


    }

    @Test
    public void testCreatingBlog() {
        doReturn(blog).when(blogRepository).save(any(Blog.class));

        BlogDto savedBlog = blogServiceImpl.createBlog(blogDto);

        assertThat(savedBlog).isNotNull();
        assertThat(savedBlog.id()).isEqualTo(blog.getId());

    }

    @Test
    public void testGettingAllBlogs() {
        doReturn(blogList).when(blogRepository).findAll();

        List<BlogDto> allBlogs = blogServiceImpl.getAllBlogs();

        assertThat(allBlogs).isNotNull();
        assertEquals(allBlogs.size(), blogList.size());


    }
}
