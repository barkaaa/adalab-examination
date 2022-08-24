package com.adalab.examination.Docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.github.dockerjava.api.model.HostConfig.newHostConfig;

@Component
public class DockerContainerFactoryBean {
    DockerClient client;

    DockerContainerFactoryBean(DockerClient client) {
        this.client = client;
    }

    public String createContainer(Map<String, String> binds, String imageId, String name, String workFile, String[] cmd) {
        HostConfig hostConfig = newHostConfig();

        List<Bind> bindList = new ArrayList<>();

        for (Map.Entry<String, String> entry : binds.entrySet()) {
            bindList.add(new Bind(entry.getKey(), new Volume(entry.getValue())));
        }
        hostConfig.setBinds(bindList.toArray(Bind[]::new));
        return client.createContainerCmd(imageId)
                .withName(name)
                .withHostConfig(hostConfig)
                .withWorkingDir(workFile)
                .withCmd(cmd)
                .exec().getId();
    }

}
