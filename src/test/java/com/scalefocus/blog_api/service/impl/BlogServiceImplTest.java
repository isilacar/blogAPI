package com.scalefocus.blog_api.service.impl;

import com.scalefocus.blog_api.dto.BlogDto;
import com.scalefocus.blog_api.entity.Blog;
import com.scalefocus.blog_api.mapper.BlogMapper;
import com.scalefocus.blog_api.repository.BlogRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class BlogServiceImplTest {

    @Mock
    private BlogRepository blogRepository;

    @Mock
    private BlogMapper blogMapper;

    @InjectMocks
    private BlogServiceImpl blogServiceImpl;

    private Blog blog;
    private BlogDto blogDto;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        PodamFactory podamFactory = new PodamFactoryImpl();

        blog = podamFactory.manufacturePojo(Blog.class);

        blogDto = BlogDto.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .text(blog.getText())
                .build();

        doReturn(blog).when(blogMapper).mapToBlog(any(BlogDto.class));
        doReturn(blogDto).when(blogMapper).mapToBlogDto(any(Blog.class));

    }

    @Test
    public void testCreatingBlog() {
        doReturn(blog).when(blogRepository).save(any(Blog.class));

        BlogDto savedBlog = blogServiceImpl.createBlog(blogDto);

        assertThat(savedBlog).isNotNull();
        assertThat(savedBlog.id()).isEqualTo(blog.getId());

    }
}
