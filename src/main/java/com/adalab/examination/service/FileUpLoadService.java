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
    public String uploadTestFile(MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                File sourceFile = new File("src/main/resources");
                String resourcePath = sourceFile.getCanonicalPath();

                String OriginalFilename = file.getOriginalFilename();//获取原文件名
                if (OriginalFilename == null) {
                    return "";
                }

                String suffixName = OriginalFilename.substring(OriginalFilename.lastIndexOf("."));//获取文件后缀名
                //重新随机生成名字
                String filename = UUID.randomUUID() + suffixName;
                File localFile = new File(resourcePath + "/testFile/" + filename);

                file.transferTo(localFile); //把上传的文件保存至本地
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

    public String uploadDockerFile(MultipartFile file) {
        try {
            File sourceFile = new File("src/main/resources");
            String resourcePath = sourceFile.getCanonicalPath();
            File localFile = new File(resourcePath + "/dockerFiles/DockerFile");
            file.transferTo(localFile);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            String now3 = df.format(System.currentTimeMillis());
            return dockerService.createImage("DockerFile",now3);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
