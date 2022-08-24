package com.adalab.examination.Docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

@Configuration
public class DockerConfig {
    @Bean
    DockerClientConfig config() throws IOException {
        Resource resource = new ClassPathResource("application.properties");
        Properties props = PropertiesLoaderUtils.loadProperties(resource);
        String host = props.getProperty("docker.url");
        return DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(host)
                .build();
    }

    @Bean
    DockerClient client(DockerClientConfig config) {
        return DockerClientBuilder.getInstance(config).build();
    }
}
