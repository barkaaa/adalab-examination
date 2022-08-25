package com.adalab.examination.controller;

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
    @PostMapping("login")
    public String manageLogin(@RequestBody Map<String,String> map){
        if (map.get("username").equals(MANAGE_NAME)){
            if(map.get("password").equals(MANAGE_PSW)){
                return "お帰りなさい";
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }
}
