package com.example.springboot_minio.controller;


import com.example.springboot_minio.service.MinioService;
import io.minio.messages.Bucket;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


import java.net.URLConnection;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class FileController {

    @Autowired
    MinioService MinioService;

    @GetMapping(path = "/buckets")
    public List<Bucket> listBuckets() {
        return MinioService.getAllBuckets();
    }

    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Map<String, String> uploadFile(@RequestPart(value = "file", required = false) MultipartFile files) throws IOException {
        Path path = Path.of(files.getOriginalFilename());
        MinioService.uploadFile(path, files.getInputStream(), files.getOriginalFilename());
        Map<String, String> result = new HashMap<>();
        result.put("key", files.getOriginalFilename());
        return result;
    }


    @GetMapping(path = "/download")
    public ResponseEntity<Resource> uploadFile(@RequestParam(value = "file") String file) throws IOException {
        Resource resource = MinioService.getFile(file);
        return ResponseEntity
                .ok()
                .header("Content-type", URLConnection.guessContentTypeFromName(file))
                .header("Content-disposition","attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);

    }
}
