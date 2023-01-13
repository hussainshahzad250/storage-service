package com.sas.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.sas.config.AwsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class CommonService {

    @Autowired
    private AwsProperties awsProperties;

    @Autowired
    private AmazonS3 amazonS3;


    @Async
    public String save(String extension) {
        String fileName = UUID.randomUUID().toString() + extension;
        return generateUrl(fileName, HttpMethod.PUT);
    }

    @Async
    public String findByName(String fileName) {
        if (!amazonS3.doesObjectExist(awsProperties.getBucketName(), fileName))
            return "File does not exist";
        log.info("Generating signed URL for file name {}", fileName);
        return generateUrl(fileName, HttpMethod.GET);
    }


    private String generateUrl(String fileName, HttpMethod httpMethod) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1); // Generated URL will be valid for 24 hours
        return amazonS3.generatePresignedUrl( awsProperties.getBucketName(), fileName, calendar.getTime(), httpMethod).toString();
    }

}
