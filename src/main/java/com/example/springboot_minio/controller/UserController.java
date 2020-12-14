package com.example.springboot_minio.controller;

import com.example.springboot_minio.exception.MinioException;
import com.example.springboot_minio.model.User;
import com.example.springboot_minio.model.dto.UserDTO;
import com.example.springboot_minio.service.UserService;
import com.example.springboot_minio.service.impl.MinIOObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin("*")
@RequestMapping("/user")
public class UserController {

    @Autowired
    MinIOObjectService minIOObjectService;

    @Autowired
    UserService userService;

    @PostMapping(value = "/createUser")
    private ResponseEntity<User> createUser(@RequestBody UserDTO userDTO) throws MinioException {
            User user = User.builder()
                    .name(userDTO.getName())
                    .company(userDTO.getCompany())
//                    .avatar(userDTO.getAvatar())
                    .build();
            userService.saveUser(user);
        return ResponseEntity
                .ok()
                .body(user);
    }

    @PostMapping("/updateAvatar")
    private ResponseEntity<User> updateAvatar(@RequestParam("id") Long id, @RequestPart("img") String img) throws MinioException {
        User user = userService.getUserById(id);
        user.setAvatar(img);
        userService.saveUser(user);
        return ResponseEntity
                .ok()
                .body(user);
    }

    @GetMapping("/userList")
    private ResponseEntity<Iterable<User>> getAll() throws MinioException {
        Iterable<User> userList = userService.getUser();
        return ResponseEntity
                .ok()
                .body(userList);
    }

    @GetMapping("/deleteUser")
    private ResponseEntity<String> deleteUser(@RequestParam("id") Long id) throws MinioException {
        User user = userService.getUserById(id);
        String img = user.getAvatar();
        userService.deleteUser(user);
        minIOObjectService.deleteObject(img);
        return ResponseEntity
                .ok()
                .body(user.getName() + "has deleted");
    }
}
