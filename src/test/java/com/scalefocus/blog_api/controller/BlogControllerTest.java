package com.scalefocus.blog_api.controller;

import com.scalefocus.blog_api.dto.BlogDto;
import com.scalefocus.blog_api.dto.TagDto;
import com.scalefocus.blog_api.request.BlogUpdateRequest;
import com.scalefocus.blog_api.request.TagAddRequest;
import com.scalefocus.blog_api.service.BlogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;

public class BlogControllerTest {

    @Mock
    private BlogService blogService;

    @InjectMocks
    private BlogController blogController;

    private BlogDto blogDto;
    private List<BlogDto> blogDtoList;
    private BlogUpdateRequest blogUpdateRequest;
    private TagAddRequest tagAddRequest;
    private TagDto tagAddDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        PodamFactory podamFactory = new PodamFactoryImpl();

        blogDto = podamFactory.manufacturePojo(BlogDto.class);
        blogDtoList = List.of(blogDto);

        blogUpdateRequest = new BlogUpdateRequest("updated title", "updated text");
        tagAddRequest= new TagAddRequest("new tag");
        tagAddDto = new TagDto(1L, tagAddRequest.tagName());
        blogDto.tagDtoSet().add(tagAddDto);

        doReturn(blogDto).when(blogService).createBlog(any(BlogDto.class));
        doReturn(blogDtoList).when(blogService).getAllBlogs();
        doReturn(blogDto).when(blogService).addTag(anyLong(),any(TagAddRequest.class));
        doReturn(blogDto).when(blogService).removeTag(anyLong(),anyLong());


    }

    @Test
    public void testCreatingBlog() {
        ResponseEntity<BlogDto> blogDtoResponseEntity = blogController.createBlog(blogDto);

        assertThat(blogDtoResponseEntity.getBody()).isNotNull();
        assertThat(blogDtoResponseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(blogDtoResponseEntity.getBody().id()).isEqualTo(blogDto.id());
    }

    @Test
    public void testGettingAllBlogs() {
        ResponseEntity<List<BlogDto>> allBlogs = blogController.getAllBlogs();

        assertThat(allBlogs.getBody()).isNotNull();
        assertEquals(allBlogs.getStatusCode(), HttpStatusCode.valueOf(200));

    }

    @Test
    public void testUpdatingBlog() {
        blogDto = BlogDto.builder().title(blogUpdateRequest.title())
                .text(blogUpdateRequest.text()).build();

        doReturn(blogDto).when(blogService).updateBlog(anyLong(), any(BlogUpdateRequest.class));

        ResponseEntity<BlogDto> blogDtoResponseEntity = blogController.updateBlog(1L, blogUpdateRequest);

        assertThat(blogDtoResponseEntity.getBody()).isNotNull();
        assertThat(blogDtoResponseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertEquals(blogDtoResponseEntity.getBody().title(), blogUpdateRequest.title());
        assertEquals(blogDtoResponseEntity.getBody().text(), blogUpdateRequest.text());

    }

    @Test
    public void testAddingTag(){
        ResponseEntity<BlogDto> blogDtoResponseEntity = blogController.addTagToBlog(1L, tagAddRequest);

        TagDto existingTag = blogDtoResponseEntity.getBody().tagDtoSet().stream().filter(tagDto -> tagDto.name().equals("new tag")).findFirst().get();

        assertThat(blogDtoResponseEntity.getBody()).isNotNull();
        assertThat(blogDtoResponseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertEquals(existingTag.name(),tagAddRequest.tagName());
    }

    @Test
    public void testDeletingTag(){
        blogDto.tagDtoSet().remove(tagAddDto);

        ResponseEntity<BlogDto> blogDtoResponseEntity = blogController.deleteTagFromBlog(1L, 1L);
        boolean notExistingTag = blogDtoResponseEntity.getBody().tagDtoSet().stream().anyMatch(tagDto -> tagDto.name().equals("new tag"));

        assertThat(blogDtoResponseEntity.getBody()).isNotNull();
        assertThat(blogDtoResponseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertFalse(notExistingTag);
    }
}