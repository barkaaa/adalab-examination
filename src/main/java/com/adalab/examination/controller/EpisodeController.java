package com.adalab.examination.controller;

import com.adalab.examination.entity.*;
import com.adalab.examination.entity.missionEntity.QuestionnaireResult;
import com.adalab.examination.service.*;
import com.adalab.examination.service.impl.OSSService;
import com.adalab.examination.vo.UploadVo;
import com.github.dockerjava.api.model.Image;
import org.apache.shiro.SecurityUtils;
import org.eclipse.jgit.api.errors.GitAPIException;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/episode")

public class EpisodeController {
    DockerService dockerService;
    FileUpLoadService fileUpLoadService;
    EpisodeService episodeService;
    StudentInfoService studentService;
    GitService gitService;
    QuestionnaireReplyService questionnaireService;

    OSSService ossService;

    EpisodeController(DockerService dockerService, FileUpLoadService fileUpLoadService,
                      EpisodeService episodeService, StudentInfoService studentService,
                      GitService gitService, QuestionnaireReplyService questionnaireService, OSSService ossService) {
        this.dockerService = dockerService;
        this.fileUpLoadService = fileUpLoadService;
        this.episodeService = episodeService;
        this.studentService = studentService;
        this.gitService = gitService;
        this.questionnaireService = questionnaireService;
        this.ossService = ossService;
    }

    /**
     * upload Docker File
     *
     * @param file docker file
     */
    @PostMapping("/docker")
    ServiceResponse<String> uploadDockerFile(@RequestPart("docker") MultipartFile file, @RequestParam("tag") String tag) {
        try {
            fileUpLoadService.uploadDockerFile(file, tag);
        } catch (RuntimeException e) {
            return new ServiceResponse<>(400, e.getMessage());
        }
        return new ServiceResponse<>(200, "????????????");
    }

    @PostMapping("/createEp")
    ServiceResponse<String> createEp(@RequestBody Episode episode) {
        try {

            if (episode.getMarkdownUrl().equals("")) {
                File file = new File("src\\main\\resources\\md\\" + UUID.randomUUID() + ".md");
                file.createNewFile();
                MultipartFile cMultiFile = new MockMultipartFile("file", file.getName(), null, new FileInputStream(file));
                UploadVo uploadVo = ossService.uploadFile(cMultiFile);
//                ????????????
                file.delete();
                episode.setMarkdownUrl(uploadVo.getUrl());
            }
            episodeService.insert(episode);
        } catch (Exception e) {
            return new ServiceResponse<>(400, e.getMessage());
        }
        return new ServiceResponse<>(200, "??????????????????");
    }


    @PatchMapping("/update")
    ServiceResponse<String> upLoadEpConfig(@RequestPart(value = "test", required = false) MultipartFile[] files,
                                           @RequestPart(value = "episode") Episode episode) {
        Episode target = episodeService.getById(episode.getId());
        if (target == null) {
            return new ServiceResponse<>(400, "??????????????????????????????");
        }
        if (files != null) {

            if (target.getTestFileName() != null) {
                File file = new File("src/main/resources/testFile/" + target.getTestFileName());
                try {
                    delete(file);
                } catch (IOException e) {
                    System.out.println("??????????????????");
                }
            }

            String newName = fileUpLoadService.uploadTestFile(files);
            episode.setTestFileName(newName);
        }
        episodeService.updateById(episode);


        return new ServiceResponse<>(200, "????????????");

    }

    @PostMapping("/test/{id}")
    ServiceResponse<TestResult> doTest(@PathVariable("id") int episodeId, @RequestBody(required = false) QuestionnaireResult questionnaireResult) throws InterruptedException {
        int id = ((MyPrincipal) SecurityUtils.getSubject().getPrincipal()).getId();
        StudentInfo studentInfo = studentService.getById(id);
        Episode episode = episodeService.getById(episodeId);
        if (studentInfo == null || episode == null) {
            return new ServiceResponse<>(400, "????????????");
        }

        if (studentInfo.getBeginDate() == null) {
            studentInfo.setBeginDate(LocalDateTime.now());
            studentInfo.setLastEdited(LocalDateTime.now());
            studentService.updateById(studentInfo);
        }

        if (episode.getType() == 2) {
            if (studentInfo.getWebPage() == null) {
                return new ServiceResponse<>(400, "?????????????????????");
            }
            try {
                gitService.gitClone(id + "", studentInfo.getWebPage(), episodeId + "");
            } catch (GitAPIException e) {
                return new ServiceResponse<>(400, "git??????????????????,?????????????????????", null);
            }

            int time = episode.getTimeOut();

            String containerId = dockerService.createContainer(episode.getImgId(), id + "",
                    episodeId, episode.getTestFileName(),
                    "/test", episode.getCmd());
            dockerService.startContainer(containerId);
            long t = System.currentTimeMillis();

            while (System.currentTimeMillis() - t <= time && dockerService.checkContainer(containerId)) {
                Thread.sleep(500);
            }
            if (dockerService.checkContainer(containerId)) {
                dockerService.stopContainer(containerId);
            }
            dockerService.removeContainer(containerId);
            TestResult testResult = dockerService.getResult(id + "", episodeId);
            if (testResult.isPassed()) {
                goNext(studentInfo, episodeId);
                return new ServiceResponse<>(200, "???????????????", testResult);
            } else {
                return new ServiceResponse<>(401, "????????????", testResult);
            }
        } else if (episode.getType() == 0) {
            goNext(studentInfo, episodeId);
            return new ServiceResponse<>(200, "???????????????");
        } else if (episode.getType() == 1) {
            if (questionnaireResult == null) {
                return new ServiceResponse<>(401, "?????????????????????");
            }
            questionnaireService.putStudentReply(questionnaireResult);
            goNext(studentInfo, episodeId);
            return new ServiceResponse<>(200, "???????????????");
        }
        return new ServiceResponse<>(400, "??????????????????,??????????????????");
    }


    void goNext(StudentInfo studentInfo, Integer episodeId) {
        studentInfo.setEpisode(episodeId);
        studentService.updateById(studentInfo);
    }

    @DeleteMapping("/delete/{id}")
    ServiceResponse<String> delete(@PathVariable("id") int id) {
        Episode episode = episodeService.getById(id);
        episodeService.delete(id);
        try {
            delete(new File("src/main/resources/testFile/" + episode.getTestFileName()));
        } catch (IOException e) {
            System.out.println("????????????????????????|?????????????????????");
        }
        return new ServiceResponse<>(200, "????????????");
    }

    void delete(File file) throws IOException {
        File[] files = file.listFiles();
        //???????????????????????????????????????????????????
        if (files != null) {
            //?????????????????????????????????????????????
            for (File file1 : files) {
                BasicFileAttributes basicFileAttributes = Files.readAttributes(file1.toPath(), BasicFileAttributes.class);
                if (basicFileAttributes.isRegularFile()) {
                    if (!file1.delete()) throw new IOException();
                }
                //???????????????????????????????????????????????????
                if (basicFileAttributes.isDirectory()) {
                    // ??????????????????,??????????????????????????????
                    delete(file1);
                    // ????????????????????????????????????,???????????????????????????,????????????????????????????????????
                    if (!file1.delete()) throw new IOException();
                }
            }
        }
        //?????????????????????????????????????????????????????????
        if (!file.delete()) throw new IOException();
    }


    @GetMapping("/images")
    ServiceResponse<List<TaggedImage>> getImages() {
        List<TaggedImage> res = new ArrayList<>();
        for (Image image : dockerService.getImages()) {
            for (String s : image.getRepoTags()) {
                res.add(new TaggedImage(s));
            }
        }
        return new ServiceResponse<>(200, "????????????", res);
    }


    @GetMapping("/get")
    ServiceResponse<List<Episode>>
    getEpisode() {
        return new ServiceResponse<>(200, "", episodeService.list());
    }


    @GetMapping("/getOne")
    ServiceResponse<Episode> getOne(int id) {
        return new ServiceResponse<>(200, "", episodeService.getById(id));
    }


    @DeleteMapping("/images")
    ServiceResponse<String> delImg(@RequestParam("id") String id) {
        try {
            dockerService.removeImage(id);
        } catch (Exception e) {
            return new ServiceResponse<>(500, "????????????");
        }

        return new ServiceResponse<>(200, "????????????");
    }

    @GetMapping("/counts")
    ServiceResponse<Integer> counts() {
        return new ServiceResponse<>(200, "", episodeService.count());
    }

    @PutMapping("/pull")
    ServiceResponse<String> pullImage(@RequestParam String image) {
        try {
            if (dockerService.pullImage(image)) {
                return new ServiceResponse<>(200, "????????????");
            } else {
                return new ServiceResponse<>(200, "???????????????,???????????????????????????");
            }
        } catch (Exception e) {
            return new ServiceResponse<>(500, "??????????????????");
        }
    }


    @PatchMapping("/updateChallengeInfo")
    public ServiceResponse<Object> updateChallengeInfo(@RequestBody Episode episode) {
        episodeService.updateById(episode);
        return new ServiceResponse<>(200, "????????????????????????");
    }


}
