package com.adalab.examination.controller;


import com.adalab.examination.entity.Student;
import com.adalab.examination.service.StudentService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
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

    final
    StudentService studentService;

    //挑战规定的时间
    private final int CHALLENGE_TIME = 5;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

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

    /**
     * 获取单个 student 的所有信息
     * @param student 学生的姓名
     * @return 学生信息
     */
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




}

