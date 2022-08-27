package com.adalab.examination.controller;

import com.adalab.examination.entity.Episode;
import com.adalab.examination.entity.Student;
import com.adalab.examination.entity.TestResult;
import com.adalab.examination.service.DockerService;
import com.adalab.examination.service.EpisodeService;
import com.adalab.examination.service.FileUpLoadService;
import com.adalab.examination.service.StudentService;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;

import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/episode")

public class EpisodeController {
    DockerService dockerService;
    FileUpLoadService fileUpLoadService;
    EpisodeService episodeService;

    StudentService studentService;

    EpisodeController(DockerService dockerService, FileUpLoadService fileUpLoadService, EpisodeService episodeService, StudentService studentService) {
        this.dockerService = dockerService;
        this.fileUpLoadService = fileUpLoadService;
        this.episodeService = episodeService;
        this.studentService = studentService;
    }

    /**
     * upload Docker File
     *
     * @param file docker file
     * @param tags 用户输入的tag
     */
    @GetMapping("/docker")
    String uploadDockerFile(@RequestPart("docker") MultipartFile file, @RequestParam("tag") String[] tags) {
        return dockerService.createImage(file.getName(), tags);
    }

    @PostMapping("/episode")
    String uploadTestFile(@RequestPart(value = "test") MultipartFile[] files, @RequestPart(value = "episode") Episode episode, HttpServletResponse response) {
        if (files.length != 0) {
            File file = new File("src/main/resources/testFile/" + episode.getTestFileUrl());
            if (file.exists()) {
                try {
                    Files.delete(Paths.get(file.toURI()));
                } catch (IOException e) {
                    response.setStatus(500);
                    return "更新文件时发生错误";
                }
            }
            String newName = fileUpLoadService.uploadTestFile(files);
            episode.setTestFileUrl(newName);
        }


        episodeService.saveOrUpdate(episode);

        return "上传成功";

    }

    @GetMapping("/test/{id}")
    TestResult doTest(@PathVariable("id") int episodeId) {
        //todo
        int stuId = 1;
        Episode episode = episodeService.getById(episodeId);
        Student student = studentService.getById(stuId);
        if (episode.getIsTest() == 0) {

        }
//        dockerService.createContainer(episode.getImgId(), );


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

    @GetMapping("/images")
    List<Image> getImages() {
        return dockerService.getImages();
    }

    @GetMapping("/episode")
    List<Episode> getEpisode() {
        return episodeService.list();
    }

}
