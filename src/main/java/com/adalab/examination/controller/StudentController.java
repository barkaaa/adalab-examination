package com.adalab.examination.controller;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Karl
 * @since 2022-08-22
 */
@RestController
@RequestMapping("/examination/student")
@CrossOrigin
public class StudentController {
    @GetMapping("/ping")
    public String ping(){
        return "pong";
    }

}

