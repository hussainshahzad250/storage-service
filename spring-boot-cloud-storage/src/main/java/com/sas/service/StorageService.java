package com.sas.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.sas.config.AwsProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Service
@AllArgsConstructor
public class StorageService {

    private final AmazonS3 s3Client;

    private final AwsProperties awsProperties;

    public String uploadFile(MultipartFile file) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(awsProperties.getBucketName(), fileName, file.getInputStream(), objectMetadata));
        return "File uploaded : " + fileName;
    }


    public byte[] downloadFile(String fileName) {
        S3Object s3Object = s3Client.getObject(awsProperties.getBucketName(), fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String deleteFile(String fileName) {
        s3Client.deleteObject(awsProperties.getBucketName(), fileName);
        return fileName + " removed ...";
    }

    public String uploadFileToPath(Long clientId, Long appId, String documentType, MultipartFile file) {
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(file.getContentType());
            objectMetadata.setContentLength(file.getSize());
            String filePath = String.format("%d/%d/%s/%s", clientId, appId, documentType, file.getOriginalFilename().replace(" ", "_"));
            s3Client.putObject(new PutObjectRequest(awsProperties.getBucketName(), filePath, file.getInputStream(), objectMetadata));
            log.info("File {} uploaded to path {}", file.getOriginalFilename(), filePath);
            return "File uploaded successfully";
        } catch (Exception exception) {
            return "File upload failed";
        }
    }
}
