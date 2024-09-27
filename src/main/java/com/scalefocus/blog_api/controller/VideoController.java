package com.scalefocus.blog_api.controller;

import com.scalefocus.blog_api.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
            description = "Create Video REST API is used to save video in a database"
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

    @Operation(
            summary = "Delete Video REST API",
            description = "Delete Video REST API is used to save user in a database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 OK"
    )
    @DeleteMapping("/{videoId}/{blogId}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long videoId, @PathVariable Long blogId) {
        videoService.deleteVideo(videoId, blogId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
