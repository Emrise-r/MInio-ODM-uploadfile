package com.example.springboot_minio.config;



import io.minio.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${minio.access.key}")
    private String accessKey;

    @Value("${minio.secret.key}")
    private String secretKey;

    @Value("${minio.url}")
    private String minioUrl;

    @Value("${minio.bucket.name}")
    private String bucketName;


    @Bean
    public MinioClient generateClient() {
        try {
            MinioClient minioClient =
                            MinioClient.builder()
                            .endpoint(minioUrl)
                            .credentials(accessKey, secretKey)
                            .build();
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                System.out.println(bucketName + " does not exist");
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucketName)
                                .build());
                System.out.println("create " + bucketName + "success");
            }
            minioClient.deleteBucketEncryption(
                    DeleteBucketEncryptionArgs
                            .builder()
                            .bucket(bucketName)
                            .build());
            return minioClient;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
