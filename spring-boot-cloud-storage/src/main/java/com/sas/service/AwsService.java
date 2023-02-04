package com.sas.service;

import com.amazonaws.HttpMethod;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.sas.config.AwsProperties;
import com.amazonaws.services.s3.AmazonS3;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

@Slf4j
@Service
@AllArgsConstructor
public class AwsService {

    private final AmazonS3 amazonS3;

    private final AwsProperties awsProperties;

    @Async
    public String uploadFile(MultipartFile file) throws IOException {
        log.info("FileName {}", file.getOriginalFilename());
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        return generateSignedUrl(fileName, HttpMethod.PUT);
    }

    private String generateSignedUrl(String fileName, HttpMethod httpMethod) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1); // Generated URL will be valid for 24 hours
        return amazonS3.generatePresignedUrl(awsProperties.getBucketName(), fileName, calendar.getTime(), httpMethod).toString();
    }

    @Async
    public String downloadFile(String fileName) {
        if (!amazonS3.doesObjectExist(awsProperties.getBucketName(), fileName))
            return "File does not exist";
        log.info("Generating signed URL for file {}", fileName);
        return generateSignedUrl(fileName, HttpMethod.GET);
    }

}
