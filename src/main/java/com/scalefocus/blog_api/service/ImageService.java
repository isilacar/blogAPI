package com.scalefocus.blog_api.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String saveImage(MultipartFile multipartFile, Long blogId, Integer imageWidth, Integer imageHeight);

    void deleteImage(Long imageId, Long blogId);
}