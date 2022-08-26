package com.adalab.examination.controller;

import com.adalab.examination.service.DockerService;
import com.adalab.examination.service.FileUpLoadService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/episode")

public class EpisodeController {
    DockerService dockerService;
    FileUpLoadService fileUpLoadService;


    EpisodeController(DockerService dockerService, FileUpLoadService fileUpLoadService) {
        this.dockerService = dockerService;
        this.fileUpLoadService = fileUpLoadService;

    }


}
