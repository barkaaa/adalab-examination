package com.adalab.examination.Docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

@Component
public class DockerImageFactoryBean {
    private final  DockerClient client;

    DockerImageFactoryBean(DockerClient client) {
        this.client = client;

    }

    public String createImage(String fileName, Set<String> tags) throws IOException {
        File file = new File("src/main/resources");
        String resourcePath = file.getCanonicalPath();
        File stuFileRepo = new File(resourcePath + "/dockerFiles/" + fileName);


        return client.buildImageCmd(stuFileRepo)
                .withTags(tags)
                .exec(new BuildImageResultCallback())
                .awaitImageId();
    }


}
