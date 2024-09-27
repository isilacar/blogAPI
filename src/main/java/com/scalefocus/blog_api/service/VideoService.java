package com.scalefocus.blog_api.service;

import org.springframework.web.multipart.MultipartFile;

public interface VideoService {

    String createVideo(MultipartFile file, Long blogId, Integer resolutionWidth, Integer resolutionHeight);

    void deleteVideo(Long videoId, Long blogId);
}
