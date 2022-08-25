package com.adalab.examination.controller;

import com.adalab.examination.entity.TestResult;
import com.adalab.examination.service.DockerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    DockerService service;

    TestController(DockerService service) {
        this.service = service;
    }

    @GetMapping("/test")
    TestResult test() throws InterruptedException {
        String imageId = service.createImage("DockerFile", "spring_test_image");
        String containerId = service.createContainer(imageId,
                "2022",
                "test.py", "/test", "test_container", "python3 test.py");
        service.startContainer(containerId);
        Thread.sleep(1000);
        service.removeContainer(containerId);
        return service.getResult("2022");
    }
}
