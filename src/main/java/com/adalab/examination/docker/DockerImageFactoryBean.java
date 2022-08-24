package com.adalab.examination.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageResultCallback;

import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Set;

@Component
public class DockerImageFactoryBean {
    DockerClient dockerClient;

    DockerImageFactoryBean(DockerClient client) {
        this.dockerClient = client;
    }

    //返回镜像ID
    public String createImage(File file, Set<String> tags) {
        BuildImageResultCallback callback = new BuildImageResultCallback();
        return dockerClient.buildImageCmd(file).withTags(tags).exec(callback).awaitImageId();
    }
}
