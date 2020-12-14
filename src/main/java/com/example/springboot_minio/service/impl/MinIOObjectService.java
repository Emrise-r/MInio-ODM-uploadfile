package com.example.springboot_minio.service.impl;

import com.example.springboot_minio.exception.MinioException;
import io.minio.*;

import io.minio.errors.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;


@Service
public class MinIOObjectService {
    private static final String imgDefault = "default/ronnin.png";

    @Value("${minio.bucket.name}")
    private String bucketName;

    @Autowired
    MinioClient minioClient;

    public void uploadFile (InputStream file, String name, Long size, String contentType) throws MinioException {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(name)
                            .stream(file, size, -1)
                            .contentType(contentType)
                            .build());
        } catch (Exception e) {
            throw new MinioException("can't upload file", e, HttpStatus.BAD_REQUEST);
        }
    }

    public Resource getFile(String name) throws MinioException {
        try {
            InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(name)
                        .build());
            Resource resource = new InputStreamResource(inputStream);
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new MinioException("file does not exist", e, HttpStatus.NOT_FOUND);
        }
    }

    public String getObjUrl(String name) throws MinioException {
        try {
            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(name)
                            .expiry(7, TimeUnit.DAYS)
                            .build()
            );
            return url;
        } catch (Exception e) {
            throw new MinioException("can't get url obj", e, HttpStatus.NOT_FOUND);
        }
    }

    public void deleteObject(String name) throws MinioException {
        if (!name.contains(imgDefault)) {
            try {
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(bucketName)
                                .object(name)
                                .build()
                );
            } catch (Exception e) {
                throw new MinioException("can't delete object'", HttpStatus.CONFLICT);
            }
        }
    }
}
