package com.adalab.examination.Docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.model.BuildResponseItem;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Set;

@Component
public class DockerImageFactoryBean {
    DockerClient client;

    DockerImageFactoryBean(DockerClient client) {
        this.client = client;
    }

    public String createImage(File file, Set<String> tags) {
        return client.buildImageCmd(file)
                .withTags(tags)
                .exec(new BuildImageResultCallback())
                .awaitImageId();
    }


}
