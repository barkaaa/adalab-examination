package com.adalab.examination.controller;

import com.adalab.examination.entity.Episode;
import com.adalab.examination.entity.TestResult;
import com.adalab.examination.service.DockerService;
import com.adalab.examination.service.EpisodeService;
import com.adalab.examination.service.FileUpLoadService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
     *
     * @param file docker file
     * @param tags 用户输入的tag
     * @return
     */
    @GetMapping("/docker")
    String uploadDockerFile(@RequestPart("docker") MultipartFile file, @RequestParam("tag[]") String[] tags) {

        return dockerService.createImage(file.getName(), tags);
    }

    @PostMapping("/episode")
    String uploadTestFile(@RequestPart(value = "test") MultipartFile[] files, @RequestPart(value = "episode") Episode episode, HttpServletResponse response) {

            String newName = fileUpLoadService.uploadTestFile(files);

            episode.setTestFileUrl(newName);

            episodeService.saveOrUpdate(episode);

            return "上传成功";

    }

    @GetMapping("/test")
    TestResult doTest() {
        //todo
        return null;
    }


    @DeleteMapping("/episode/{id}")
    String delete(@PathVariable("id") int id) {
        Episode episode = episodeService.getById(id);
        episodeService.removeById(id);
        try {
            Files.delete(Paths.get("src/main/resources/testFile/" + episode.getTestFileUrl()));
        } catch (IOException e) {
            return "删除失败";
        }
        return "删除成功";
    }

}
