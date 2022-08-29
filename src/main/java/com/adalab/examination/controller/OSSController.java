package com.adalab.examination.controller;

import com.adalab.examination.service.impl.OSSService;
import com.adalab.examination.vo.UploadVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

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
    public UploadVo uploadFiles(@RequestParam("file") MultipartFile file) {
        return ossService.uploadFile(file);
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

    /**
     * 根据文件内容保存为markdown
     *
     * @param map
     * @return
     */
    @PutMapping("/saveMarkdown")
    public UploadVo saveMarkdown(@RequestBody Map<String, String> map) {
        return ossService.saveMarkdown(map.get("content"));
    }
}