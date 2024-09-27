package com.scalefocus.blog_api.controller;

import com.scalefocus.blog_api.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(
        name = "CRUD REST APIs for Video Resource",
        description = "CRUD REST APIs - Create Video, Delete Video, Get Video"
)
@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @Operation(
            summary = "Create Video REST API",
            description = "Create Video REST API is used to save video in a database with file path url"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status 201 CREATED"
    )
    @PostMapping
    public String createVideo(@RequestParam("video") MultipartFile file,
                              @RequestParam Long blogId,
                              @RequestParam Integer resolutionWidth,
                              @RequestParam Integer resolutionHeight) {

        return videoService.createVideo(file, blogId, resolutionWidth, resolutionHeight);
    }

}
