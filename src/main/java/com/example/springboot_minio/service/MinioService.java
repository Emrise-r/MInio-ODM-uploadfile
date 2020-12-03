package com.example.springboot_minio.service;

import io.minio.MinioClient;
import io.minio.PutObjectOptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;


@Service
public class MinioService {

    @Autowired
    MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;

    public void uploadFile (Path source, InputStream file, String contentType) {
        try {
            PutObjectOptions options = new PutObjectOptions(file.available(), -1);
            options.setContentType(contentType);
            minioClient.putObject(bucketName, source.toString(), file, options);
        } catch (Exception e) {
            throw new RuntimeException("error upload file", e);
        }
    }


    public Resource getFile(String file) throws IOException {
        try {
            InputStream is = minioClient.getObject(bucketName, file);
            InputStreamResource inputStreamResource = new InputStreamResource(is);
            Resource resource = inputStreamResource;
            return resource;
        } catch (Exception e) {
            throw new RuntimeException("error read file", e);
        }
    }

//    public void getAccessFile(String file) {
//        try {
//            minioClient.presignedPutObject()
//        } catch (Exception e) {
//            throw new RuntimeException("error get access file", e);
//        }
//    }
}
