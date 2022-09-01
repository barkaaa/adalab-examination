package com.adalab.examination.controller;

import com.adalab.examination.entity.ServiceResponse;
import com.adalab.examination.entity.StudentInfoToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

/**
 * 管理员接口
 */
@RestController
@RequestMapping("/api/manage")
@CrossOrigin
public class ManageController {

    private final String MANAGE_NAME = "adalab";

    private final String MANAGE_PSW = "1919810";

    @PostMapping("/login")
    public ServiceResponse<String> manageLogin(@RequestBody Map<String, String> map) {
        Subject subject= SecurityUtils.getSubject();
        //封装用户的登录数据
        StudentInfoToken token = new StudentInfoToken(map.get("username"),map.get("password"),"root");
        try {
            subject.login(token);
            return new ServiceResponse<>(200, "登录成功");
        }catch (AuthenticationException e){
            return new ServiceResponse<>(403,"用户名或密码错误");
        }
    }
}
