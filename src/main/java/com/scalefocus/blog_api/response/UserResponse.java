package com.scalefocus.blog_api.response;

import com.scalefocus.blog_api.dto.BlogDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long userId;
    private List<BlogDto> blogList;
}