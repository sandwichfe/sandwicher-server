package com.lww.oss.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.checksums.RequestChecksumCalculation;
import software.amazon.awssdk.core.checksums.ResponseChecksumValidation;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

/**
 * RustFS 配置管理，通过 S3 协议访问对象存储。
 *
 * @author lww
 */
@Configuration
@ConditionalOnProperty(prefix = "oss.service", name = "type", havingValue = "rustfs")
@Data
public class RustfsConfig {

    /**
     * RustFS S3 API 地址，不是管理控制台地址
     */
    @Value("${rustfs.url}")
    private String url;

    /**
     * RustFS 访问账户
     */
    @Value("${rustfs.accessKey}")
    private String accessKey;

    /**
     * RustFS 访问密钥
     */
    @Value("${rustfs.secretKey}")
    private String secretKey;

    /**
     * S3 签名区域，与 RustFS 服务端配置保持一致
     */
    @Value("${rustfs.region:us-east-1}")
    private String region;

    /**
     * 构造 RustFS S3 客户端，使用路径式寻址并在容器关闭时释放连接。
     */
    @Bean(destroyMethod = "close")
    public S3Client rustfsClient() {
        return S3Client.builder()
                .endpointOverride(URI.create(url))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .region(Region.of(region))
                .forcePathStyle(true)
                // 仅在协议要求时计算或校验额外校验和，兼容 S3 对象存储服务。
                .requestChecksumCalculation(RequestChecksumCalculation.WHEN_REQUIRED)
                .responseChecksumValidation(ResponseChecksumValidation.WHEN_REQUIRED)
                .build();
    }

    /**
     * 预签名客户端使用相同的地址、凭据和路径式寻址配置。
     */
    @Bean(destroyMethod = "close")
    public S3Presigner rustfsPresigner() {
        return S3Presigner.builder()
                .endpointOverride(URI.create(url))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .region(Region.of(region))
                // 预签名链接用于浏览器直接访问，不要求调用方提供校验和请求头。
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .checksumValidationEnabled(false)
                        .build())
                .build();
    }

}

