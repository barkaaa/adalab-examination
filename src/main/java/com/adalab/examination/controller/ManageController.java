package com.adalab.examination.controller;

import com.adalab.examination.entity.ServiceResponse;
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
        if (map.get("username").equals(MANAGE_NAME)) {
            if (map.get("password").equals(MANAGE_PSW)) {
                return new ServiceResponse<>(200, "登录成功");
            }
        }
        return new ServiceResponse<>(403,"用户名或密码错误");
    }
}
