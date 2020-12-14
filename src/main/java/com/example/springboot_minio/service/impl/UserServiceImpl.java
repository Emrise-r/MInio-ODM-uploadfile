package com.example.springboot_minio.service.impl;

import com.example.springboot_minio.exception.MinioException;
import com.example.springboot_minio.model.User;
import com.example.springboot_minio.repository.UserRepository;
import com.example.springboot_minio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final String imgDefault = "default/ronnin.png";

    @Autowired
    MinIOObjectService minIOObjectService;

    @Autowired
    UserRepository userRepository;

    @Override
    public Iterable<User> getUser() throws MinioException {
        Iterable<User> userList = userRepository.findAll();
        for (User user : userList) {
            String avatar = minIOObjectService.getObjUrl(user.getAvatar());
            if(avatar != null) {
                user.setAvatar(avatar);
            }
        }
        return userList;
    }

    @Override
    public void saveUser(User user) throws MinioException {
        if (user.getAvatar() == null) {
            user.setAvatar(imgDefault);
        } else if (minIOObjectService.getObjUrl(user.getAvatar()) == null) {
            user.setAvatar(imgDefault);
        }
        userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) {
         userRepository.delete(user);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findUserById(userId);
    }
}
