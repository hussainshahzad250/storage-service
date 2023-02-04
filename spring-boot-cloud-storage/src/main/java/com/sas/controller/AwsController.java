package com.sas.controller;

import com.sas.service.AwsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Upload and download file using signed URL
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class AwsController {

    private final AwsService awsService;

    @PostMapping("/generateUploadUrl")
    public ResponseEntity<String> generateUploadUrl(@RequestParam(value = "file") MultipartFile file) throws IOException {
        log.info("Request initiated to generate signed URL for file {}", file.getOriginalFilename());
        return new ResponseEntity<>(awsService.uploadFile(file), HttpStatus.OK);
    }

    @GetMapping("/generateDownloadUrl/{fileName}")
    public String generateDownloadUrl(@PathVariable("fileName") String fileName) {
        return awsService.downloadFile(fileName);
    }
}
