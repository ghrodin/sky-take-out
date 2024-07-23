package com.sky.utils;

import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Data
@AllArgsConstructor
@Slf4j
public class MinioUtil {

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;

    /**
     * 文件上传
     *
     * @param bytes      文件字节数组
     * @param objectName 对象名称
     * @return 文件访问路径
     */
    public String upload(byte[] bytes, String objectName) {

        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();

        try {
            // 确保桶存在
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                throw new RuntimeException(bucketName+"not exists");
            }

            // 上传文件
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(new ByteArrayInputStream(bytes), bytes.length, -1)
                            .build()
            );

            // 构造文件访问路径
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder
                    .append(endpoint)
                    .append("/")
                    .append(bucketName)
                    .append("/")
                    .append(objectName);

            log.info("文件上传到: {}", stringBuilder.toString());
            return stringBuilder.toString();

        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("上传文件时发生错误: ", e);
            return null;
        }
    }
}
