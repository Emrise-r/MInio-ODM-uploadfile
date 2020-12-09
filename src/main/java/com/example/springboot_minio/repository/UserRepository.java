package com.example.springboot_minio.repository;

import com.example.springboot_minio.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserById(Long id);
}
