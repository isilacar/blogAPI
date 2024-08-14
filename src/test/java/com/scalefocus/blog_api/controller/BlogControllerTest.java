package com.scalefocus.blog_api.controller;

import com.scalefocus.blog_api.dto.BlogDto;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

public class BlogControllerTest {

    @Mock
    private BlogService blogService;

    @InjectMocks
    private BlogController blogController;

    private BlogDto blogDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        PodamFactory podamFactory = new PodamFactoryImpl();

        blogDto = podamFactory.manufacturePojo(BlogDto.class);

        doReturn(blogDto).when(blogService).createBlog(any(BlogDto.class));

    }

    @Test
    public void testCreatingBlog() {
        ResponseEntity<BlogDto> blogDtoResponseEntity = blogController.createBlog(blogDto);

        assertThat(blogDtoResponseEntity.getBody()).isNotNull();
        assertThat(blogDtoResponseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(blogDtoResponseEntity.getBody().id()).isEqualTo(blogDto.id());
    }
}