package com.example.springboot_minio.controller;

import com.example.springboot_minio.model.User;
import com.example.springboot_minio.model.dto.UserDTO;
import com.example.springboot_minio.service.UserService;
import com.example.springboot_minio.service.impl.MinIOObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLConnection;
import java.util.Iterator;

@RestController
@CrossOrigin("*")
@RequestMapping("/user")
public class UserController {

    @Autowired
    MinIOObjectService minIOObjectService;

    @Autowired
    UserService userService;

    @PostMapping(value = "/createUser")
    private ResponseEntity<User> createUser(@RequestBody UserDTO userDTO) throws IOException {
            User user = User.builder()
                    .name(userDTO.getName())
                    .company(userDTO.getCompany())
                    .build();
            userService.saveUser(user);
        return ResponseEntity
                .ok()
                .body(user);
    }

    @PostMapping("/updateAvatar")
    private ResponseEntity<User> updateAvatar(@RequestPart("id") Long id, @RequestPart("img") String img) {
        User user = userService.getUserById(id);
        user.setAvatar(img);
        userService.saveUser(user);
        return ResponseEntity
                .ok()
                .body(user);
    }

    @GetMapping("/userList")
    private ResponseEntity<Iterable<User>> getAll() {
        Iterable<User> userList = userService.getUser();
        return ResponseEntity
                .ok()
                .body(userList);
    }
}
