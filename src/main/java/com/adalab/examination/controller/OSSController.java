package com.adalab.examination.controller;

import com.adalab.examination.service.impl.OSSService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.util.List;

/**
 * @author wupx
 * @date 2020/07/09
 */
@RestController
@RequestMapping("/api/oss")
public class OSSController {

    private final OSSService ossService;

    public OSSController(OSSService ossService) {
        this.ossService = ossService;
    }

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/uploadFiles")
    public String uploadFiles(@RequestParam("file") MultipartFile file, @RequestParam("storagePath") String storagePath) {
        return ossService.uploadFile(file, storagePath);
    }

    /**
     * 下载文件
     *
     * @param fileName
     */
    @PostMapping(value = "/exportFile")
    public void exportFile(OutputStream os, @RequestParam("fileName") String fileName) {
        ossService.exportFile(os, fileName);
    }

    /**
     * 删除文件
     *
     * @param fileName
     */
    @PostMapping(value = "/deleteFile")
    public void deleteFile(@RequestParam("fileName") String fileName) {
        ossService.deleteFile(fileName);
    }

    /**
     * 查看文件列表
     *
     * @return
     */
    @PostMapping(value = "/listObjects")
    public List<String> listObjects() {
        return ossService.listObjects();
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName
     */
    @PostMapping(value = "/doesObjectExist")
    public boolean doesObjectExist(@RequestParam("fileName") String fileName) {
        return ossService.doesObjectExist(fileName);
    }

    /**
     * 获取 url
     *
     * @param fileName
     * @param expSeconds
     */
    @GetMapping(value = "/getSingeNatureUrl")

    public String getSingeNatureUrl(@RequestParam("fileName") String fileName, @RequestParam("expSeconds") int expSeconds) {
        return ossService.getSingeNatureUrl(fileName, expSeconds);
    }



    /**
     * 设置文件访问权限
     *
     * @param fileName
     */
    @PostMapping(value = "/setObjectAcl")
    public void setObjectAcl(@RequestParam("fileName") String fileName) {
        ossService.setObjectAcl(fileName);
    }
}