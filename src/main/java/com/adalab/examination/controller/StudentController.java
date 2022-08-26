package com.adalab.examination.controller;


import com.adalab.examination.entity.Student;
import com.adalab.examination.service.StudentService;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.adalab.examination.GitClone.DirectoryUtils.traverseDir;
import static com.adalab.examination.GitClone.GitUtils.delAllFile;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Karl
 * @since 2022-08-22
 */
@RestController
@RequestMapping("/api/student")
@CrossOrigin
public class StudentController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    StudentService studentService;

    //挑战规定的时间
    private final int CHALLENGE_TIME = 5;

    @GetMapping("/ping")
    public String ping(){
        return "pong";
    }

    /**
     *
     * @return 所有学生数据组成的LIST
     */
    @GetMapping("getRanking")
    public List<Student> getRanking(){
        LambdaQueryWrapper<Student> studentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        studentLambdaQueryWrapper.orderByDesc(Student::getRanking);
        return studentService.list(studentLambdaQueryWrapper);
    }

    @GetMapping("getStudent/{id}")
    public Student getStudent(@PathVariable int id){
        Student student = studentService.getById(id);
        if(student!=null){
            return student;
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 前端点下开始闯关按钮，调用这个接口，数据库中放入开始时间
     * @param id 根据id查学生
     * @return 这个学生的挑战截至时间
     */
    @GetMapping("begin/{id}")
    public LocalDateTime begin(@PathVariable int id){
        Student student = studentService.getById(id);
        LocalDateTime localDateTime = LocalDateTime.now();
        student.setBeginTime(localDateTime);
        studentService.updateById(student);
        return localDateTime.plusDays(CHALLENGE_TIME);
    }

    @PostMapping("/getDetail")
    public Student getDetail(@NotNull @RequestBody Student student){
        LambdaQueryWrapper<Student> studentQuery = new LambdaQueryWrapper<>();
        studentQuery.eq(Student::getName,student.getName());
        Student one = studentService.getOne(studentQuery);
        return one;
    }

    /**根据需求会有一个闯关页面收集学员更详细信息
     *
     * @param student
     * @return
     */
    @PostMapping("updateStudent")
    public String update(@RequestBody Student student){
        if(studentService.updateById(student)){
            return "success";
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    //在resources文件夹下根据学员姓名生成文件夹 clone学员的代码
    @PostMapping("/studentCode")
    public String cloneCode(@RequestBody Map<String,String>map){
        String step = "/step"+map.get("step");
        String gitURL = map.get("url");
        String path = System.getProperty("user.dir");
        String name = "/"+map.get("name");
        String time = "/"+System.currentTimeMillis();
        String localPath = path+"/src/main/resources/studentCode"+name+step+time;
        File file = new File(localPath);
        if (file.exists()){
            logger.info("删除文件结果:"+delAllFile(localPath));
        }

        try {
            Git git = Git.cloneRepository()
                    .setURI(gitURL)
                    .setDirectory(new File(localPath))
                    .call();
            logger.info("clone成功!"+git.toString());
        } catch (GitAPIException e) {
            logger.error("clone失败!");
            e.printStackTrace();
            return "克隆失败";
        }
        return "OK";
    }


    //获取学员代码提交历史记录的文件结构树 精确到关卡
    @PostMapping("/studentCode/FilesTree/{name}")
    public  HashMap<String, Object> getFilesTree(@RequestBody Map<String,String>map,@PathVariable String name){
        String userName = "/"+name;
        String step = "/step"+map.get("step");
        String localPath = System.getProperty("user.dir")+"/src/main/resources/studentCode"+userName+step;
        HashMap<String, Object> hashMap = traverseDir(localPath);
        logger.info("获取到结构树");
        return hashMap;
    }


}

