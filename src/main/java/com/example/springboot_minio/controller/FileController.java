package com.example.springboot_minio.controller;


import com.example.springboot_minio.service.MinIOBucketService;
import com.example.springboot_minio.service.MinIOObjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class FileController {

    @Autowired
    MinIOObjectService minIOObjectService;

    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Map<String, String>> uploadFile(@RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        minIOObjectService.uploadFile(file.getInputStream(), file.getOriginalFilename(), file.getSize(), contentType);
        Map<String, String> result = new HashMap<>();
        result.put("key", file.getOriginalFilename());
        return ResponseEntity
                .ok()
                .header("Content-type", URLConnection.guessContentTypeFromName(contentType))
                .body(result);
    }


    @GetMapping(path = "/download")
    public ResponseEntity<?> uploadFile(@RequestParam(value = "file") String file) {
        Resource resource = minIOObjectService.getFile(file);
        if(resource == null) {
            return new ResponseEntity("file not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity
                .ok()
                .header("Content-type", URLConnection.guessContentTypeFromName(file))
                .header("Content-disposition","attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }



    @GetMapping("/getObjUrl")
    public ResponseEntity<String> getObjUrl(@RequestParam("file") String file) {
        return ResponseEntity
                .ok()
                .body(minIOObjectService.getObjUrl(file));
    }
}
