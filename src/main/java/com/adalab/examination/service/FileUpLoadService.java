package com.adalab.examination.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileUpLoadService {

    private final DockerService dockerService;

    FileUpLoadService(DockerService dockerService) {
        this.dockerService = dockerService;
    }

    /**
     * @return 返回""则失败
     */
    public String uploadTestFile(MultipartFile[] files) {
        if (files.length != 0) {
            try {
                File sourceFile = new File("src/main/resources");
                String resourcePath = sourceFile.getCanonicalPath();
                //重新随机生成名字
                String filename = UUID.randomUUID() + "";
                File newFile = new File(resourcePath + "/testFile/" + filename);
                newFile.mkdirs();
                for (MultipartFile f : files) {
                    File temp = new File(newFile.getCanonicalPath() + "/" + f.getOriginalFilename());
                    f.transferTo(temp);
                }
                return filename;
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("上传失败");
                return "";
            }
        } else {
            System.out.println("文件为空");
            return "";
        }
    }

    public String uploadDockerFile(MultipartFile file, String tag) throws RuntimeException {
        try {
            File sourceFile = new File("src/main/resources");
            String resourcePath = sourceFile.getCanonicalPath();
            File localFile = new File(resourcePath + "/dockerFiles/DockerFile");
            if (!localFile.getParentFile().exists()) {
                if (!localFile.getParentFile().mkdirs()) {
                    throw new RuntimeException("镜像文件目录生成失败");
                }
            }
            file.transferTo(localFile);
            return dockerService.createImage("DockerFile", tag);
        } catch (IOException e) {
            throw new RuntimeException("文件写入失败");
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
