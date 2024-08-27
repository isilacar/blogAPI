package com.scalefocus.blog_api;

import com.scalefocus.blog_api.dto.BlogDto;
import com.scalefocus.blog_api.dto.TagDto;
import com.scalefocus.blog_api.entity.Blog;
import com.scalefocus.blog_api.request.BlogUpdateRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.TestPropertySource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
class BlogApiApplicationTests {

    @LocalServerPort
    private int portNumber;

    private String baseUrl;

    @Autowired
    private BlogTestH2Repository blogTestH2Repository;

    private static TestRestTemplate restTemplate;

    @BeforeAll
    public static void init() {
        restTemplate = new TestRestTemplate();
    }

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + portNumber + "/api/blogs";
    }

    @Test
    public void testAddingBlog() {
        Set<TagDto> tagDtoSet = new HashSet<>();
        TagDto tagDto = TagDto.builder()
                .name("integration tag")
                .build();
        tagDtoSet.add(tagDto);
        BlogDto blogDto = BlogDto.builder()
                .title("integration title")
                .text("integration text")
                .tagDtoSet(tagDtoSet)
                .build();

        BlogDto savedBlogDto = restTemplate.postForEntity(baseUrl, blogDto, BlogDto.class).getBody();

        assertEquals("integration title", savedBlogDto.title());
        assertThat(blogTestH2Repository.findAll().size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void testGettingAllBlogs() {
        List<BlogDto> blogDtoListResponse = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<BlogDto>>() {
                }
        ).getBody();

        assertThat(blogDtoListResponse.size()).isGreaterThanOrEqualTo(1);
        assertThat(blogTestH2Repository.findAll().size()).isGreaterThanOrEqualTo(1);

    }

    @Test
    public void testUpdatingBlog() {
        String updateBlogUrl= baseUrl + "/{blogId}";

        BlogUpdateRequest blogUpdateRequest = new BlogUpdateRequest("updated title", "updated text");

        restTemplate.put(updateBlogUrl, blogUpdateRequest, 2L);
        Blog blogFromDB = blogTestH2Repository.findById(2L).get();

        assertAll(
                () -> assertNotNull(blogFromDB),
                () -> assertEquals("updated text", blogFromDB.getText()),
                () -> assertEquals("updated title", blogFromDB.getTitle())
        );
    }

}
