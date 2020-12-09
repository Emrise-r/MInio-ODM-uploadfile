package com.example.springboot_minio.service;

import com.example.springboot_minio.model.User;

public interface UserService {

    User getUserById(Long userId);

    Iterable<User> getUser();

    void saveUser(User user);

    void deleteUser(User user);

}
