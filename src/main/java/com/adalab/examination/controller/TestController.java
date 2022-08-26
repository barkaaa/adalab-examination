package com.adalab.examination.controller;

import com.adalab.examination.entity.TestResult;
import com.adalab.examination.service.DockerService;
import com.adalab.examination.service.FileUpLoadService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("api/test")
public class TestController {
    DockerService service;
    FileUpLoadService fileUpLoadService;

    TestController(DockerService service, FileUpLoadService fileUpLoadService) {
        this.service = service;
        this.fileUpLoadService = fileUpLoadService;
    }

    @GetMapping("/test")
    TestResult test() throws InterruptedException {
        String imageId = service.createImage("DockerFile", "spring_test_image");
        String containerId = service.createContainer(imageId,
                "2022",
                "test.py", "/test", "test_container", "python3 test.py");

        service.startContainer(containerId);

        while (!service.checkContainer(containerId)) {
            Thread.sleep(500);
        }

        service.removeContainer(containerId);
        return service.getResult("2022");
    }

    /**
     * 1.文件保存在服务器，url地址保存在数据库
     * 上传成功之后返回成功保存的url地址
     */

    @PostMapping("/uploadT")
    public String uploadT(@RequestPart("test") MultipartFile file) {
        return fileUpLoadService.uploadTestFile(file);
    }

    @PostMapping("/uploadD")
    public String uploadD(@RequestPart("docker") MultipartFile file) {
        return fileUpLoadService.uploadDockerFile(file);
    }
}
