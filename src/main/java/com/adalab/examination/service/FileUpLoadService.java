package com.adalab.examination.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
                if (!newFile.mkdir()) {
                    return "";
                }
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

    public String uploadDockerFile(MultipartFile file, String tag) {
        try {
            File sourceFile = new File("src/main/resources");
            String resourcePath = sourceFile.getCanonicalPath();
            File localFile = new File(resourcePath + "/dockerFiles/DockerFile");
            file.transferTo(localFile);
            return dockerService.createImage("DockerFile",  tag);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
