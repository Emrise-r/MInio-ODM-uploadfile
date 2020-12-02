package com.example.springboot_minio.controller;


import com.example.springboot_minio.service.MinioService;
import io.minio.messages.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
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
//        Adapter.uploadFile(files.getOriginalFilename(), files.getBytes());
        Path path = Path.of(files.getOriginalFilename());
        MinioService.uploadFile(path, files.getInputStream(), files.getOriginalFilename());
        Map<String, String> result = new HashMap<>();
        result.put("key", files.getOriginalFilename());
        return result;
    }

//    @GetMapping(path = "/download")
//    public ResponseEntity<ByteArrayResource> uploadFile(@RequestParam(value = "key") String file) throws IOException {
//        byte[] data = Adapter.getFile(file);
//        ByteArrayResource resource = new ByteArrayResource(data);
//        return ResponseEntity
//                .ok()
//                .contentLength(data.length)
//                .header("Content-type", "application/octet-stream")
//                .header("Content-disposition", "filename=\"" + file + "\"")
//                .body(resource);
//
//    }

    @GetMapping(path = "/download")
    public ResponseEntity<ByteArrayResource> uploadFile(@RequestParam(value = "key") String file) throws IOException {
       InputStream inputStream = MinioService.getFile(Path.of(file));
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "filename=\"" + file + "\"")
                .body(resource);

    }
}
