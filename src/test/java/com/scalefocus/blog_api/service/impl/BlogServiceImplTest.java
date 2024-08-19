package com.scalefocus.blog_api.service.impl;

import com.scalefocus.blog_api.dto.BlogDto;
import com.scalefocus.blog_api.entity.Blog;
import com.scalefocus.blog_api.exception.ResourceNotFound;
import com.scalefocus.blog_api.mapper.BlogMapper;
import com.scalefocus.blog_api.repository.BlogRepository;
import com.scalefocus.blog_api.request.BlogUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

public class BlogServiceImplTest {

    public static final long BLOG_ID = 1L;
    public static final String ERROR_MESSAGE = "Blog does not exist with id: " + BLOG_ID;

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
    private BlogUpdateRequest blogUpdateRequest;
    private ResourceNotFound resourceNotFound;

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

        blogUpdateRequest = new BlogUpdateRequest("updated title", "updated text");

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

    @Test
    public void testReturnUpdatedBlog_whenBlogFound() {
       blogDto= BlogDto.builder()
               .title(blogUpdateRequest.title())
               .text(blogUpdateRequest.text())
               .build();

        doReturn(Optional.ofNullable(blog)).when(blogRepository).findById(anyLong());
        doReturn(blogDto).when(blogMapper).mapToBlogDto(any(Blog.class));

        BlogDto foundBlog = blogServiceImpl.updateBlog(BLOG_ID, blogUpdateRequest);

        assertThat(foundBlog).isNotNull();
        assertEquals(foundBlog.title(), blogUpdateRequest.title());
        assertEquals(foundBlog.text(), blogUpdateRequest.text());
    }

    @Test
    public void testThrowException_whenBlogNotFound() {
        doReturn(Optional.empty()).when(blogRepository).findById(anyLong());

        ResourceNotFound assertThrows = assertThrows(ResourceNotFound.class, () -> blogServiceImpl.updateBlog(BLOG_ID, blogUpdateRequest),
                "Should throw exception when blog not found");

        assertThat(assertThrows).hasMessage(ERROR_MESSAGE);
    }

}
