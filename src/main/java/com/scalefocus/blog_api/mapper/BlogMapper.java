package com.scalefocus.blog_api.mapper;


import com.scalefocus.blog_api.dto.BlogDto;
import com.scalefocus.blog_api.entity.Blog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlogMapper {

    private final TagMapper tagMapper;

    public BlogDto mapToBlogDto(Blog blog) {
        return BlogDto.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .text(blog.getText())
                .tagDtoSet(tagMapper.mapToTagDtoList(blog.getTags()))
                .build();
    }

    public Blog mapToBlog(BlogDto blogDto) {
       return Blog.builder().id(blogDto.id())
                .title(blogDto.title())
                .text(blogDto.text())
                .tags(tagMapper.mapToTagList(blogDto.tagDtoSet()))
                .build();

    }

}
