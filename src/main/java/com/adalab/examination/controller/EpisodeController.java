package com.adalab.examination.controller;

import com.adalab.examination.entity.TestResult;
import com.adalab.examination.service.DockerService;
import com.adalab.examination.service.EpisodeService;
import com.adalab.examination.service.FileUpLoadService;
import com.adalab.examination.service.impl.EpisodeServiceImpl;
import org.springframework.web.bind.annotation.*;

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

    String uploadDockerFile() {
        //todo
        return null;
    }

    String uploadTestFile() {
        //todo
        return null;
    }

    TestResult doTest() {
        //todo
        return null;
    }


}
