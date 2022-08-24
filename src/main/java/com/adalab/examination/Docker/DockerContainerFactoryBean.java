package com.adalab.examination.Docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.*;
import org.springframework.stereotype.Component;

import static com.github.dockerjava.api.model.HostConfig.newHostConfig;

@Component
public class DockerContainerFactoryBean {
    DockerClient client;

    DockerContainerFactoryBean(DockerClient client) {
        this.client = client;
    }

    public String createContainer(String stu, String testCode, String imageId, String name, String workFile, String[] cmd) {
        HostConfig hostConfig = newHostConfig();
        Bind stuCodeMounting = new Bind(stu, new Volume(workFile));
        Bind testCodeMounting = new Bind(testCode, new Volume(workFile + stu.substring(stu.lastIndexOf("/"))));

        hostConfig.setBinds(stuCodeMounting, testCodeMounting);
        return client.createContainerCmd(imageId)
                .withName(name)
                .withHostConfig(hostConfig)
                .withWorkingDir(workFile)
                .withCmd(cmd)
                .exec().getId();
    }

}
