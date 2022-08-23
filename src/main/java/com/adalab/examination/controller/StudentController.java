package com.adalab.examination.controller;


import com.adalab.examination.entity.Student;
import com.adalab.examination.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @Autowired
    StudentService studentService;

    @GetMapping("/ping")
    public String ping(){
        return "pong";
    }

    @GetMapping("getRanking")
    public List<Student> getRanking(){
        return studentService.list();
    }

}

