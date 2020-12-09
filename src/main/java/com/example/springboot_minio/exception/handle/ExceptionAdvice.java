package com.example.springboot_minio.exception.handle;

import com.example.springboot_minio.exception.MinioException;
import com.example.springboot_minio.model.dto.MessageResponseDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler({MinioException.class})
    public ResponseEntity handleException( final MinioException ex) {
        final MessageResponseDTO messageResponse = new MessageResponseDTO("error", ex.getMessage(), ex.getStatus().getReasonPhrase(), String.valueOf(ex.getStatus().value()));
        return  new ResponseEntity(messageResponse, new HttpHeaders(), ex.getStatus());
    }
}
