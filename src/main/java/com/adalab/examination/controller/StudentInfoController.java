package com.adalab.examination.controller;


import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import com.adalab.examination.entity.Student;
import com.adalab.examination.entity.StudentInfo;
import com.adalab.examination.service.StudentInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.adalab.examination.GitClone.DirectoryUtils.traverseDir;
import static com.adalab.examination.GitClone.GitUtils.delAllFile;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Karl
 * @since 2022-08-27
 */
@RestController
@RequestMapping("/api/examination/student-info")
public class StudentInfoController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    //获取学员代码提交历史记录的文件结构树 精确到关卡
    @PostMapping("/studentCode/FilesTree/{name}")
    public HashMap<String, Object> getFilesTree(@RequestBody Map<String,String>map, @PathVariable String name){
        String userName = "/"+name;
        String step = "/step"+map.get("step");
        String localPath = System.getProperty("user.dir")+"/src/main/resources/studentCode"+userName+step;
        HashMap<String, Object> hashMap = traverseDir(localPath);
        logger.info("获取到结构树");
        return hashMap;
    }

    final
    StudentInfoService studentInfoService;

    public StudentInfoController(StudentInfoService studentInfoService) {
        this.studentInfoService = studentInfoService;
    }

    /** zxear专用
     * 获取单个 student 的所有信息
     * @param studentInfo 学生的姓名
     * @return 学生信息
     */
    @PostMapping("/getDetail")
    public HashMap<Object, Object> getDetail(@NotNull @RequestBody StudentInfo studentInfo) {
        LambdaQueryWrapper<StudentInfo> studentQuery = new LambdaQueryWrapper<>();
        studentQuery.eq(StudentInfo::getName, studentInfo.getName());
        StudentInfo one = studentInfoService.getOne(studentQuery);
        HashMap<Object, Object> objectHashMap = new HashMap<>();
        objectHashMap.put("name",one.getName());
        objectHashMap.put("Date Created",one.getCreatedDate());
        objectHashMap.put("Days needed",one.getDaysNeeded());
        objectHashMap.put("Actual Days",one.getActualDays());
        objectHashMap.put("Last Edited",one.getLastEdited());
        objectHashMap.put("Current Week",one.getId());
        objectHashMap.put("type",one.getType());
        return objectHashMap;
    }

    /**
     * @return 所有学生数据组成的LIST
     */
    @GetMapping("getList")
    public List<StudentInfo> getList(){
        LambdaQueryWrapper<StudentInfo> studentInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        studentInfoLambdaQueryWrapper.Desc(StudentInfo::getRanking);
        return studentInfoService.list(studentInfoLambdaQueryWrapper);
    }
}

