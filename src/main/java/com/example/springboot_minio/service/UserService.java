package com.example.springboot_minio.service;

import com.example.springboot_minio.exception.MinioException;
import com.example.springboot_minio.model.User;

public interface UserService {

    User getUserById(Long userId);

    Iterable<User> getUser() throws MinioException;

    void saveUser(User user) throws MinioException;

    void deleteUser(User user);

}
