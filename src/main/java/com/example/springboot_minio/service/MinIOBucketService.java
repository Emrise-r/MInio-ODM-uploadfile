package com.example.springboot_minio.service;

import io.minio.GetBucketPolicyArgs;
import io.minio.MinioClient;
import io.minio.messages.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MinIOBucketService {

    @Value("${minio.bucket.name}")
    private String bucketName;

    @Autowired
    MinioClient minioClient;

    public String getPolicy() {
        try {
            String config = minioClient.getBucketPolicy(GetBucketPolicyArgs.builder().bucket(bucketName).build());
            return config;
        } catch (Exception e) {
            throw new RuntimeException("can't get policy", e);
        }
    }

    public List<String> listBucket() {
        try {
            List<String> bucketList = new ArrayList<String>();
            for(Bucket bucket :minioClient.listBuckets()) {
                bucketList.add(bucket.name());
            }
            return bucketList;
        } catch (Exception e) {
            throw new RuntimeException("empty", e);
        }
    }
}
