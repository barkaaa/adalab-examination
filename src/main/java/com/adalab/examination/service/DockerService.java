package com.adalab.examination.service;

import com.adalab.examination.Docker.DockerContainerFactoryBean;
import com.adalab.examination.Docker.DockerImageFactoryBean;
import com.adalab.examination.entity.TestResult;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.model.Image;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class DockerService {

    private final DockerClient dockerClient;
    private final DockerImageFactoryBean imageFactoryBean;
    private final DockerContainerFactoryBean containerFactoryBean;
    //在application.properties中获取
    private final String resultFileName;

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

    /**
     * @param fileName dockerFile位置
     * @param tags     生产镜像的标签
     * @return 镜像的服务器ID
     */
    public String createImage(String fileName, String... tags) throws RuntimeException {
        try {
            return imageFactoryBean.createImage(fileName, Arrays.stream(tags).collect(Collectors.toSet()));
        } catch (Exception e) {
            throw new RuntimeException("生成镜像文件失败");
        }
    }

    /**
     * @param imageId         镜像id
     * @param stuCodeFileName 学生代码文件名
     * @param testFileName    测试文件名
     * @param workDir         容器测试目录(学生代码和测试代码会挂载到一个目录)
     * @param runCMD          测试文件命令行运行代码
     * @return 生成容器在docker服务的位置
     */

    public String createContainer(String imageId, String stuCodeFileName, int ep,
                                  String testFileName, String workDir, String runCMD) {
        String[] cmd = runCMD.split(" ");

        try {
            return containerFactoryBean.createContainer(findLastPost(stuCodeFileName, ep), testFileName, imageId, workDir, cmd);
        } catch (IOException e) {
            return null;
        }
    }


    /**
     * @param stuFileName 学员代码文件夹名称
     * @return 测试文件结果
     */
    public TestResult getResult(String stuFileName, int ep) {
        try {
            String lastPost = findLastPost(stuFileName, ep);
            File res = new File(lastPost + "/" + resultFileName);
            return new TestResult(res);
        } catch (IOException e) {
            TestResult result = new TestResult();
            result.getMes().put("err", "测试文件未生成");
            return result;
        }

    }

    private String findLastPost(String stuFileName, int ep) throws IOException {
        File file = new File("src/main/resources");
        String resourcePath = file.getCanonicalPath();
        File stuFileRepo = new File(resourcePath + "/studentCode/" + stuFileName + "/step" + ep);
        var files = stuFileRepo.list();
        if (files == null) throw new RuntimeException("目录没有文件");
        var lastCodeFile = Arrays.stream(files).min((a, b) -> -a.compareTo(b));
        if (lastCodeFile.isEmpty()) throw new RuntimeException("目录没有文件");
        return stuFileRepo.getCanonicalPath() + "/" + lastCodeFile.get();
    }

    public void startContainer(String containerId) {
        dockerClient.startContainerCmd(containerId).exec();
    }

    public void stopContainer(String containerId) {
        dockerClient.stopContainerCmd(containerId).exec();
    }

    public void removeContainer(String containerId) {
        dockerClient.removeContainerCmd(containerId).exec();
    }

    public void removeImage(String imageId) {
        dockerClient.removeImageCmd(imageId).exec();
    }

    public boolean checkContainer(String containerId) {
        return !dockerClient.listContainersCmd().withIdFilter(List.of(containerId)).exec().isEmpty();
    }

    public List<Image> getImages() {
        return dockerClient.listImagesCmd().exec();
    }


    public boolean pullImage(String image) throws InterruptedException {
        PullImageResultCallback callback = new PullImageResultCallback();
        return dockerClient.pullImageCmd(image).exec(callback).awaitCompletion(5, TimeUnit.MINUTES);
    }

}
