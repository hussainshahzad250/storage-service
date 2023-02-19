package com.sas.controller;

import com.sas.service.StorageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Upload and download file to AWS
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class StorageController {

    private final StorageService storageService;

    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file) throws IOException {
        log.info("Request initiated to upload file {} to AWS", file.getOriginalFilename());
        return new ResponseEntity<>(storageService.uploadFile(file), HttpStatus.OK);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName) {
        log.info("Request initiated to download file {} ", fileName);
        byte[] data = storageService.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        return new ResponseEntity<>(storageService.deleteFile(fileName), HttpStatus.OK);
    }


    @PostMapping("/uploadFileToPath")
    public ResponseEntity<String> uploadFileToPath(@RequestParam Long clientId, @RequestParam Long appId, @RequestParam String documentType, @RequestParam(value = "file") MultipartFile file) {
        log.info("Request initiated to upload file {} to AWS", file.getOriginalFilename());
        return new ResponseEntity<>(storageService.uploadFileToPath(clientId, appId, documentType, file), HttpStatus.OK);
    }

    @GetMapping("/downloadFromPath")
    public ResponseEntity<ByteArrayResource> downloadFromPath(@RequestParam String fileName) {
        log.info("Request initiated to download file {} ", fileName);
        byte[] data = storageService.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=" + fileName)
                .body(resource);
    }
}
