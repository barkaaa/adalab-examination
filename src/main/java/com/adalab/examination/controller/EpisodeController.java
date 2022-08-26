package com.adalab.examination.controller;

import com.adalab.examination.entity.TestResult;
import com.adalab.examination.service.DockerService;
import com.adalab.examination.service.EpisodeService;
import com.adalab.examination.service.FileUpLoadService;
import com.adalab.examination.service.impl.EpisodeServiceImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/episode")

public class EpisodeController {
    DockerService dockerService;
    FileUpLoadService fileUpLoadService;
    EpisodeService episodeService;


    EpisodeController(DockerService dockerService, FileUpLoadService fileUpLoadService, EpisodeService episodeService) {
        this.dockerService = dockerService;
        this.fileUpLoadService = fileUpLoadService;
        this.episodeService = episodeService;
    }

    /**
     * upload Docker File
     * @param file docker file
     * @param tags 用户输入的tag
     * @return
     */
    @GetMapping("/docker")
    String uploadDockerFile(@RequestPart("docker") MultipartFile file,@RequestParam("tag[]")String[]tags) {


        return dockerService.createImage(file.getName(),tags);
    }

    String uploadTestFile() {
        //todo
        return null;
    }

    @GetMapping("/test")
    TestResult doTest() {
        //todo
        return null;
    }


}
