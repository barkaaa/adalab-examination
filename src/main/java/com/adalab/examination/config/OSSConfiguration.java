package com.adalab.examination.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class OSSConfiguration {

    private volatile static OSS ossClient;

    private volatile static OSSClientBuilder ossClientBuilder;

    public static String endpoint;

    private static String accessKeyId;

    private static String accessKeySecret;

    @Value("${aliyun.bucketName}")
    private String bucketName;

    @Value("${aliyun.endpoint}")
    public void setEndpoint(String endpoint) {
        OSSConfiguration.endpoint = endpoint;
    }

    @Value("${aliyun.accessKeyId}")
    public void setAccessKeyId(String accessKeyId) {
        OSSConfiguration.accessKeyId = accessKeyId;
    }

    @Value("${aliyun.accessKeySecret}")
    public void setAccessKeySecret(String accessKeySecret) {
        OSSConfiguration.accessKeySecret = accessKeySecret;
    }

    public String getBucketName() {
        return bucketName;
    }

    @Bean
    @Scope("prototype")
    public static OSS initOSSClient() {
        if (ossClient == null) {
            synchronized (OSSConfiguration.class) {
                if (ossClient == null) {
                    ossClient = initOSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
                }
            }
        }
        return ossClient;
    }

    public static OSSClientBuilder initOSSClientBuilder() {
        if (ossClientBuilder == null) {
            synchronized (OSSConfiguration.class) {
                if (ossClientBuilder == null) {
                    ossClientBuilder = new OSSClientBuilder();
                }
            }
        }
        return ossClientBuilder;
    }

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
