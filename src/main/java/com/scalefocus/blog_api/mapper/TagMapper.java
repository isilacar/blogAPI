package com.scalefocus.blog_api.mapper;

import com.scalefocus.blog_api.dto.TagDto;
import com.scalefocus.blog_api.entity.Tag;

import java.util.Set;
import java.util.stream.Collectors;

public class TagMapper {

    public static Tag mapToTag(TagDto tagDto) {
        Tag tag = new Tag();
        tag.setId(tagDto.id());
        tag.setName(tagDto.name());

        return tag;
    }

    public static TagDto mapToTagDto(Tag tag) {
       return new TagDto(tag.getId(), tag.getName());

    }
    public static Set<TagDto> mapToTagDtoList(Set<Tag> tags) {
        return tags.stream().map(TagMapper::mapToTagDto).collect(Collectors.toSet());
    }
    public static Set<Tag> mapToTagList(Set<TagDto> tagDtos) {
        return tagDtos.stream().map(TagMapper::mapToTag).collect(Collectors.toSet());
    }
}
