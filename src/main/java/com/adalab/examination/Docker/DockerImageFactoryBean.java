package com.adalab.examination.Docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Set;

@Component
public class DockerImageFactoryBean {
    DockerClient client;

    DockerImageFactoryBean(DockerClient client) {
        this.client = client;

    }

    public String createImage(String fileName, Set<String> tags) throws IOException {
        File file = new File("src/main/resources");
        String resourcePath = file.getCanonicalPath();
        return client.buildImageCmd(new File(resourcePath + "/dockerFiles/" + fileName))
                .withTags(tags)
                .exec(new BuildImageResultCallback())
                .awaitImageId();
    }


}
