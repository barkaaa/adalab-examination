package com.adalab.examination.controller;

import com.adalab.examination.entity.TestResult;
import com.adalab.examination.service.DockerService;
import com.adalab.examination.service.FileUpLoadService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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
        String imageId = service.createImage("DockerFile", "spring_test_image2");
        String containerId = service.createContainer(imageId,
                "Ayaya", 1,
                "test.py", "/test", "test_container", "python3 test.py");

        service.startContainer(containerId);

        while (!service.checkContainer(containerId)) {
            Thread.sleep(500);
        }

        service.removeContainer(containerId);
        return service.getResult("Ayaya", 1);
    }

    /**
     * 1.文件保存在服务器，文件名保存在数据库
     * 上传成功之后返回文件名
     */

    @PostMapping("/uploadT")
    public String uploadT(@RequestPart("test") MultipartFile file) {
        return fileUpLoadService.uploadTestFile(file);
    }

    /**
     * 上传dockerFile自动生成镜像不进行保存
     */
    @PostMapping("/uploadD")
    public String uploadD(@RequestPart("docker") MultipartFile file, @RequestPart("name") String name) {
        return fileUpLoadService.uploadDockerFile(file, name);
    }
}
