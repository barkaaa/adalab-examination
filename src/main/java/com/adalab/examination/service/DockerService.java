package com.adalab.examination.service;

import com.adalab.examination.Docker.DockerContainerFactoryBean;
import com.adalab.examination.Docker.DockerImageFactoryBean;
import com.adalab.examination.entity.TestResult;
import com.github.dockerjava.api.DockerClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DockerService {

    DockerClient dockerClient;
    DockerImageFactoryBean imageFactoryBean;
    DockerContainerFactoryBean containerFactoryBean;
    //在application.properties中获取
    String resultFileName;

    DockerService(DockerClient dockerClient,
                  DockerImageFactoryBean imageFactoryBean,
                  DockerContainerFactoryBean containerFactoryBean) throws IOException {
        this.dockerClient = dockerClient;
        this.imageFactoryBean = imageFactoryBean;
        this.containerFactoryBean = containerFactoryBean;
        Resource resource = new ClassPathResource("application.properties");
        Properties props = PropertiesLoaderUtils.loadProperties(resource);
        this.resultFileName = props.getProperty("test.resultFileName");

    }


    public String createImage(String fileName, String... tags) {
        try {
            return imageFactoryBean.createImage(fileName, Arrays.stream(tags).collect(Collectors.toSet()));
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * @param imageId         镜像id
     * @param stuCodeFileName 学生代码文件名
     * @param testFileName    测试文件名
     * @param workDir         容器测试目录(学生代码和测试代码会挂载到一个目录)
     * @param name            容器名
     * @param runCMD          测试文件命令行运行代码
     * @return 生成容器在docker服务的位置
     */

    public String createContainer(String imageId, String stuCodeFileName,
                                  String testFileName, String workDir,
                                  String name, String runCMD) {
        int index = 0;
        for (int i = 0; i < runCMD.length(); i++) {
            if (runCMD.charAt(i) == ' ') {
                index = i;
                break;
            }
        }
        String[] cmd = new String[2];
        cmd[0] = runCMD.substring(0, index);
        cmd[1] = runCMD.substring(index + 1);
        try {
            return containerFactoryBean.createContainer(stuCodeFileName, testFileName, imageId, name, workDir, cmd);
        } catch (IOException e) {
            return null;
        }
    }


    /**
     * @param stuFileName 学员代码文件夹名称
     * @return 测试文件结果
     */
    public TestResult getResult(String stuFileName) {
        File file = new File("src/main/resources");
        try {
            String resourcePath = file.getCanonicalPath();
            File res = new File(resourcePath + "/studentCode/" + stuFileName + "/" + resultFileName);
            return new TestResult(res);
        } catch (IOException e) {
            TestResult result = new TestResult();
            result.getMes().put("err", "测试文件未生成");
            return result;
        }

    }

    public void startContainer(String containerId) {
        dockerClient.startContainerCmd(containerId).exec();
    }

    public void removeContainer(String containerId) {
        dockerClient.removeContainerCmd(containerId).exec();
    }

    public void removeImage(String imageId) {
        dockerClient.removeImageCmd(imageId).exec();
    }
}
