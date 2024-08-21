package com.scalefocus.blog_api.service.impl;

import com.scalefocus.blog_api.dto.BlogDto;
import com.scalefocus.blog_api.dto.TagDto;
import com.scalefocus.blog_api.entity.Blog;
import com.scalefocus.blog_api.entity.Tag;
import com.scalefocus.blog_api.exception.ResourceNotFound;
import com.scalefocus.blog_api.mapper.BlogMapper;
import com.scalefocus.blog_api.repository.BlogRepository;
import com.scalefocus.blog_api.repository.TagRepository;
import com.scalefocus.blog_api.request.BlogUpdateRequest;
import com.scalefocus.blog_api.request.TagAddRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

public class BlogServiceImplTest {

    public static final long BLOG_ID = 1L;
    private static final Long TAG_ID = 1L;
    public static final String BLOG_NOT_FOUND_ERROR_MESSAGE = "Blog does not exist with id: " + BLOG_ID;
    public static final String BLOG_DOES_NOT_HAVE_TAG_ERROR_MESSAGE = "Blog with id:" + BLOG_ID + " does not have any tag with id: " + TAG_ID;

    @Mock
    private BlogRepository blogRepository;

    @Mock
    private BlogMapper blogMapper;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private BlogServiceImpl blogServiceImpl;

    private Blog blog;
    private BlogDto blogDto;
    private List<Blog> blogList;
    private List<BlogDto> blogDtoList;
    private BlogUpdateRequest blogUpdateRequest;
    private TagAddRequest tagAddRequest;
    private Tag tag;
    private TagDto tagDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        PodamFactory podamFactory = new PodamFactoryImpl();

        blog = podamFactory.manufacturePojo(Blog.class);
        blogList = List.of(blog);

        blogUpdateRequest = new BlogUpdateRequest("updated title", "updated text");
        tagAddRequest = new TagAddRequest("new tag");

        tag = Tag.builder()
                .id(TAG_ID)
                .name(tagAddRequest.tagName())
                .build();
        blog.getTags().add(tag);

        Set<TagDto> tagDtoSet = blog.getTags().stream()
                .map(tag -> new TagDto(tag.getId(), tag.getName()))
                .collect(Collectors.toSet());

        blogDto = BlogDto.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .text(blog.getText())
                .tagDtoSet(tagDtoSet)
                .build();
        blogDtoList = List.of(blogDto);

        tagDto = TagDto.builder()
                .id(TAG_ID)
                .name(tagAddRequest.tagName())
                .build();
        blogDto.tagDtoSet().add(tagDto);

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
        blogDto = BlogDto.builder()
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

        assertThat(assertThrows).hasMessage(BLOG_NOT_FOUND_ERROR_MESSAGE);
    }

    @Test
    public void testAddingNewTag_returnBlogDto_whenBlogFound() {
        doReturn(Optional.ofNullable(blog)).when(blogRepository).findById(anyLong());
        BlogDto foundedBlogDto = blogServiceImpl.addTag(1L, tagAddRequest);
        TagDto foundedTagDto = foundedBlogDto.tagDtoSet().stream().filter(tagDto -> tagDto.name().equals("new tag")).findFirst().get();

        assertThat(foundedBlogDto).isNotNull();
        assertEquals(foundedTagDto.name(), tagAddRequest.tagName());

    }


    @Test
    public void testAddingNewTag_ThrowException_whenBlogNotFound() {
        doReturn(Optional.empty()).when(blogRepository).findById(anyLong());
        ResourceNotFound assertThrows = assertThrows(ResourceNotFound.class, () -> blogServiceImpl.addTag(BLOG_ID, tagAddRequest),
                "Should throw exception when blog not found");
        assertThat(assertThrows).hasMessage(BLOG_NOT_FOUND_ERROR_MESSAGE);

    }

    @Test
    public void testRemovingTag_returnBlogDto_whenBlogAndTagFound() {
        blogDto.tagDtoSet().remove(tagDto);
        doReturn(Optional.ofNullable(blog)).when(blogRepository).findById(anyLong());

        BlogDto foundedBlogDto = blogServiceImpl.removeTag(BLOG_ID, TAG_ID);

        boolean notExistingTag = foundedBlogDto.tagDtoSet().stream().anyMatch(tagDto -> tagDto.name().equals("new tag"));

        assertThat(foundedBlogDto).isNotNull();
        assertFalse(notExistingTag);

    }

    @Test
    public void testRemovingTag_throwException_whenBlogNotFound() {
        doReturn(Optional.empty()).when(blogRepository).findById(anyLong());
        ResourceNotFound assertThrows = assertThrows(ResourceNotFound.class, () -> blogServiceImpl.removeTag(BLOG_ID, TAG_ID),
                "Should throw exception when blog not found");
        assertThat(assertThrows).hasMessage(BLOG_NOT_FOUND_ERROR_MESSAGE);

    }

    @Test
    public void testRemovingTag_throwException_whenBlogDoesNotHaveSpecificTag() {
        blog.getTags().remove(tag);
        doReturn(Optional.ofNullable(blog)).when(blogRepository).findById(anyLong());

        ResourceNotFound assertThrows = assertThrows(ResourceNotFound.class, () -> blogServiceImpl.removeTag(BLOG_ID, TAG_ID),
                "Should throw exception when blog does not have specific tag");

        assertThat(assertThrows).hasMessage(BLOG_DOES_NOT_HAVE_TAG_ERROR_MESSAGE);
    }

}
