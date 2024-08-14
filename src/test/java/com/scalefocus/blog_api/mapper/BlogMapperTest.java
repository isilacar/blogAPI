package com.scalefocus.blog_api.mapper;

import com.scalefocus.blog_api.dto.BlogDto;
import com.scalefocus.blog_api.dto.TagDto;
import com.scalefocus.blog_api.entity.Blog;
import com.scalefocus.blog_api.entity.Tag;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class BlogMapperTest {

    @Mock
    private TagMapper tagMapper;

    @InjectMocks
    private BlogMapper blogMapper;

    private Blog blog;
    private BlogDto blogDto;
    private Set<Tag> tags;
    private Set<TagDto> tagDtoSet;


    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        PodamFactory podamFactory = new PodamFactoryImpl();

        blog = podamFactory.manufacturePojo(Blog.class);
        tags = blog.getTags();
        tagDtoSet = tags.stream()
                .map(tag -> new TagDto(tag.getId(), tag.getName()))
                .collect(Collectors.toSet());


        blogDto = BlogDto.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .text(blog.getText())
                .tagDtoSet(tagDtoSet)
                .build();

        doReturn(tagDtoSet).when(tagMapper).mapToTagDtoList(anySet());

        doReturn(tags).when(tagMapper).mapToTagList(anySet());

    }

    @Test
    public void testMapToBlogDto() {
        BlogDto returnedBlogDto = blogMapper.mapToBlogDto(blog);

        assertThat(returnedBlogDto).isNotNull();
        assertThat(returnedBlogDto.title()).isEqualTo(blogDto.title());
        assertThat(returnedBlogDto.tagDtoSet().size()).isEqualTo(tagDtoSet.size());
        assertThat(returnedBlogDto.id()).isEqualTo(blog.getId());

    }

    @Test
    public void testMapToBlog() {
        Blog returnedBlog = blogMapper.mapToBlog(blogDto);

        assertThat(returnedBlog).isNotNull();
        assertThat(returnedBlog.getTitle()).isEqualTo(blog.getTitle());
        assertThat(returnedBlog.getId()).isEqualTo(blog.getId());
        assertThat(returnedBlog.getTags()).isEqualTo(blog.getTags());
    }
}