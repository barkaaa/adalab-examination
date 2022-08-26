package com.adalab.examination.controller;

import com.adalab.examination.entity.Episode;
import com.adalab.examination.entity.TestResult;
import com.adalab.examination.service.DockerService;
import com.adalab.examination.service.EpisodeService;
import com.adalab.examination.service.FileUpLoadService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

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

    @PostMapping("/createEpisode")
    String uploadTestFile(@RequestPart("test") MultipartFile file, @RequestPart("episode") Episode episode, HttpServletResponse response) {
        String newName = fileUpLoadService.uploadTestFile(file);
        if (file.getOriginalFilename() == null) {
            response.setStatus(400);
            return "400 BadRequest";
        }
        episode.setTestFileUrl(newName);
        episode.setCmd(episode.getCmd().replaceFirst(file.getOriginalFilename(), newName));
        episodeService.save(episode);

        return "上传成功";
    }

    TestResult doTest() {
        //todo
        return null;
    }


}
