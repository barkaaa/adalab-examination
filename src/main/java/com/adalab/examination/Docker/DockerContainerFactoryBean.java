package com.adalab.examination.Docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

import static com.github.dockerjava.api.model.HostConfig.newHostConfig;

@Component
public class DockerContainerFactoryBean {
    private final DockerClient client;

    DockerContainerFactoryBean(DockerClient client) {
        this.client = client;

    }

    public String createContainer(String stuFilePath, String testFileName, String imageId, String name, String workFile, String[] cmd) throws IOException {
        HostConfig hostConfig = newHostConfig();
        File file = new File("src/main/resources");
        String resourcePath = file.getCanonicalPath();

        Bind stuCodeMounting = new Bind(stuFilePath, new Volume(workFile + "/target"));

        Bind testCodeMounting = new Bind(resourcePath + "/testFile/" + testFileName, new Volume(workFile + "/" + testFileName));

        hostConfig.setBinds(stuCodeMounting, testCodeMounting);
        return client.createContainerCmd(imageId)
                .withName(name)
                .withHostConfig(hostConfig)
                .withWorkingDir(workFile + "/" + testFileName)
                .withCmd(cmd)
                .exec().getId();
    }


}
