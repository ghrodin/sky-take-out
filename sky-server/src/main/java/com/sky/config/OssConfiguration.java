package com.sky.config;

import com.sky.properties.MinioOssProperties;
import com.sky.utils.MinioUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class OssConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public MinioUtil minioUtil(MinioOssProperties minioOssProperties) {
        log.info("开始上传minio文件上传对象{}",minioOssProperties);
        return new MinioUtil(minioOssProperties.getEndpoint(),
                minioOssProperties.getAccessKey(),
                minioOssProperties.getSecretKey(),
                minioOssProperties.getBucketName());
    }
}
