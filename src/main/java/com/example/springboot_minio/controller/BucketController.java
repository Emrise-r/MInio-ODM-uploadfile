package com.example.springboot_minio.controller;

import com.example.springboot_minio.exception.MinioException;
import com.example.springboot_minio.service.impl.MinIOBucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
public class BucketController {

    @Autowired
    MinIOBucketService minIOBucketService;

    @GetMapping("/getPolicy")
    public ResponseEntity<String> getPolicy() throws MinioException {
        return ResponseEntity
                .ok()
                .body(minIOBucketService.getPolicy());
    }

    @GetMapping("/listBucket")
    public ResponseEntity<List<String>> listBucket() {
        List<String> bucketList = minIOBucketService.listBucket();
        return new ResponseEntity<>(bucketList, HttpStatus.OK);
    }
}
