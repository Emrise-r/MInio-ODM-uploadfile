package com.example.springboot_minio.service;

import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import io.minio.messages.Bucket;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.file.ConfigurationSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

@Service
public class MinioService {

    @Autowired
    MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;

    public List<Bucket> getAllBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

//    public void uploadFile(String name, byte[] content) {
//        File file = new File(name);
//        file.canWrite();
//        file.canRead();
//        try {
//            FileOutputStream iofs = new FileOutputStream(file);
//            iofs.write(content);
//            minioClient.putObject(bucketName, "/" + name, file.getAbsolutePath());
//        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage());
//        }
//    }

    public void uploadFile (Path source, InputStream file, String contentType) {
        try {
            PutObjectOptions options = new PutObjectOptions(file.available(), -1);
            options.setContentType(contentType);
            minioClient.putObject(bucketName, source.toString(), file, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Resource getFile(String file) throws IOException {
        try {
//            Path pathFile = Path.of(bucketName)
            InputStream is = minioClient.getObject(bucketName, file);
            InputStreamResource inputStreamResource = new InputStreamResource(is);
            Resource resource = inputStreamResource;
            return resource;
        } catch (Exception e) {
            throw new RuntimeException("error read file", e);
        }
    }
}
