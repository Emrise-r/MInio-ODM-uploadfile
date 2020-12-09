package com.example.springboot_minio.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class MessageResponseDTO<T> {
    private T data;
    private String message;
    private String status;
    private String code;


    public MessageResponseDTO(T data, String message, String status, String code) {
        this.data = data;
        this.message = message;
        this.status = status;
        this.code = code;
    }
}
