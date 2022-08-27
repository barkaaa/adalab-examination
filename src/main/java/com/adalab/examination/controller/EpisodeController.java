package com.adalab.examination.controller;

import com.adalab.examination.entity.Episode;
import com.adalab.examination.entity.StudentInfo;
import com.adalab.examination.entity.TestResult;
import com.adalab.examination.service.DockerService;
import com.adalab.examination.service.EpisodeService;
import com.adalab.examination.service.FileUpLoadService;
import com.adalab.examination.service.StudentInfoService;
import com.github.dockerjava.api.model.Image;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

@RestController
@RequestMapping("/api/episode")

public class EpisodeController {
    DockerService dockerService;
    FileUpLoadService fileUpLoadService;
    EpisodeService episodeService;

    StudentInfoService studentService;

    EpisodeController(DockerService dockerService, FileUpLoadService fileUpLoadService, EpisodeService episodeService, StudentInfoService studentService) {
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
    String createEp(@RequestPart(value = "test", required = false) MultipartFile[] files, @RequestPart(value = "episode") Episode episode, HttpServletResponse response) {
        if (files != null) {
            File file = new File("src/main/resources/testFile/" + episode.getTestFileName());
            if (file.exists()) {
                try {
                    Files.delete(Paths.get(file.toURI()));
                } catch (IOException e) {
                    response.setStatus(500);
                    return "更新文件时发生错误";
                }
            }
            String newName = fileUpLoadService.uploadTestFile(files);
            episode.setTestFileName(newName);
        }


        episodeService.insert(episode);

        return "上传成功";

    }

    @GetMapping("/test/{id}")
    TestResult doTest(@PathVariable("id") int episodeId) throws InterruptedException {
        //todo
        int stuId = 1;
        Episode episode = episodeService.getById(episodeId);

        int time = episode.getTimeOut();

        String containerId = dockerService.createContainer(episode.getImgId(), stuId + "",
                episodeId, episode.getTestFileName(),
                "/test", "testContainer", episode.getCmd());

        dockerService.startContainer(containerId);
        long t = System.currentTimeMillis();

        while (System.currentTimeMillis() - t <= time && !dockerService.checkContainer(containerId)) {
            Thread.sleep(500);
        }
        return dockerService.getResult(stuId + "", episodeId);

    }


    @DeleteMapping("/episode/{id}")
    String delete(@PathVariable("id") int id) {
        Episode episode = episodeService.getById(id);
        episodeService.delete(id);
        try {
            delete(new File("src/main/resources/testFile/" + episode.getTestFileName()));
        } catch (IOException e) {
            e.printStackTrace();
            return "删除失败";
        }
        return "删除成功";
    }

    void delete(File file) throws IOException {
        File[] files = file.listFiles();
        //循环遍历数组中的所有子文件和文件夹
        if (files != null) {
            //判断是否是文件，如果是，就删除
            for (File file1 : files) {
                BasicFileAttributes basicFileAttributes = Files.readAttributes(file1.toPath(), BasicFileAttributes.class);
                if (basicFileAttributes.isRegularFile()) {
                    if (!file1.delete()) throw new IOException();
                }
                //在循环中，判断遍历出的是否是文件夹
                if (basicFileAttributes.isDirectory()) {
                    // 如果是文件夹,就递归删除里面的文件
                    delete(file1);
                    // 删除该文件夹里所有文件后,当前文件夹就为空了,那么就可以删除该文件夹了
                    if (!file1.delete()) throw new IOException();
                }
            }
        }
        //删除完里面的文件夹后，当前文件夹也删除
        if (!file.delete()) throw new IOException();
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
