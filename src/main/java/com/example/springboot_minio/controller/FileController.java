package com.example.springboot_minio.controller;


import com.example.springboot_minio.exception.MinioException;
import com.example.springboot_minio.model.User;
import com.example.springboot_minio.service.UserService;
import com.example.springboot_minio.service.impl.MinIOObjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class FileController {

    private static final List<String> contentTypeImage = Arrays.asList("image/png", "image/jpeg", "image/jpg");

    @Autowired
    MinIOObjectService minIOObjectService;

    @Autowired
    UserService userService;

    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Map<String, String>> uploadFile(@RequestPart(value = "file", required = false) MultipartFile file, @RequestParam("id") Long id) throws IOException, IllegalAccessException, MinioException {
        String contentType = file.getContentType();
//        if (!contentTypeImage.contains(contentType)) {
//            throw new MinioException("Photos must be in png, jpg, jpeg format", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
//        }
        User user = userService.getUserById(id);
        if (user == null) {
            throw new MinioException("user not found");
        }
        String userAvatar = user.getName() + file.getOriginalFilename();
        minIOObjectService.uploadFile(file.getInputStream(), userAvatar, file.getSize(), contentType);
        String url = minIOObjectService.getObjUrl(file.getOriginalFilename());
        Map<String, String> result = new HashMap<>();
        result.put("key", userAvatar);
        return ResponseEntity
                .ok()
                .header("Content-type", URLConnection.guessContentTypeFromName(contentType))
                .body(result);
    }


    @GetMapping(path = "/download")
    public ResponseEntity<?> uploadFile(@RequestParam(value = "file") String file) throws MinioException {
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
    public ResponseEntity<String> getObjUrl(@RequestParam("file") String file) throws MinioException {
        return ResponseEntity
                .ok()
                .body(minIOObjectService.getObjUrl(file));
    }
    @GetMapping("/deleteObj")
    public ResponseEntity<?> deleteObj(@RequestParam("file") String file) throws MinioException {
        if (minIOObjectService.getObjUrl(file) == null) {
            return new ResponseEntity("file not found", HttpStatus.NOT_FOUND);
        }
        minIOObjectService.deleteObject(file);
        return ResponseEntity
                .ok()
                .body("deleted");
    }
}
