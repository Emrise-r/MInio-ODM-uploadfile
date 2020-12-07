package com.example.springboot_minio.exception;

import org.springframework.http.HttpStatus;

public class MinioException extends Exception {
    private HttpStatus status;
    private int code;

    public MinioException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }

    public MinioException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }


}
