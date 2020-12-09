package com.example.springboot_minio.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Builder
@ToString
public class UserDTO {

        private Long id;

        private String name;

        private String company;

        private String avatar;
}
