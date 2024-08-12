package com.scalefocus.blog_api.mapper;


import com.scalefocus.blog_api.dto.BlogDto;
import com.scalefocus.blog_api.entity.Blog;

public class BlogMapper {

    public static BlogDto mapToBlogDto(Blog blog) {

        return new BlogDto(
                blog.getId(),
                blog.getTitle(),
                blog.getText(),
                TagMapper.mapToTagDtoList(blog.getTags())

        );
    }

    public static Blog mapToBlog(BlogDto blogDto) {
        Blog blog = new Blog();
        blog.setId(blogDto.id());
        blog.setTitle(blogDto.title());
        blog.setText(blogDto.text());
        blog.setTags(TagMapper.mapToTagList(blogDto.tagDtoSet()));
        return blog;
    }



}
