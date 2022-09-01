package com.adalab.examination.controller;

import com.adalab.examination.entity.ServiceResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/error")
public class ErrorController {
    @RequestMapping("/401")
    public ServiceResponse<String> error(){
        return new ServiceResponse<>(401,"没有权限",null);
    }
}
