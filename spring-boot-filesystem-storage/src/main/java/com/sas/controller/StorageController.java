package com.sas.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sas.exception.ObjectNotFoundException;
import com.sas.service.StorageService;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class StorageController {

    @Autowired
    private StorageService service;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImageToFIleSystem(@RequestParam("file") MultipartFile file) throws IOException {
        String uploadImage = service.uploadImageToFileSystem(file);
        return ResponseEntity.status(HttpStatus.OK).body(uploadImage);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String fileName, HttpServletResponse response)
            throws IOException, ObjectNotFoundException {
        byte[] imageData = service.downloadImageFromFileSystem(fileName);
        ByteArrayResource resource = new ByteArrayResource(imageData);
        return ResponseEntity
                .ok()
                .contentLength(imageData.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=" + fileName)
                .body(resource);
    }

    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws ObjectNotFoundException, IOException {
        service.downloadImage(fileName, response);
    }
}
