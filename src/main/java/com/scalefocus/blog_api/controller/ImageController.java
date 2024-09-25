package com.scalefocus.blog_api.controller;

import com.scalefocus.blog_api.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(
        name = "CRUD REST APIs for Image Resource",
        description = "CRUD REST APIs - Create Image, Get Image, Delete Image"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    @Operation(
            summary = "Save Image REST API",
            description = "Save Image REST API is used to save image in database and also to file"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status 201 CREATED"
    )
    @PostMapping
    public ResponseEntity<String> saveImage(@RequestParam("file") MultipartFile image,
                                            @RequestParam Long blogId,
                                            @RequestParam Integer imageWidth,
                                            @RequestParam Integer imageHeight) {
        return new ResponseEntity<>(imageService.saveImage(image, blogId, imageWidth, imageHeight), HttpStatus.CREATED);
    }
}