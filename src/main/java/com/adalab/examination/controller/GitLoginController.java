package com.adalab.examination.controller;


import com.adalab.examination.entity.AccessTokenDTO;
import com.adalab.examination.entity.GitHubUser;
import com.adalab.examination.entity.StudentInfo;
import com.adalab.examination.service.GitLoginService;
import com.adalab.examination.service.StudentInfoService;
import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import okhttp3.RequestBody;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@RequestMapping
@CrossOrigin
public class GitLoginController {



    StudentInfoService studentInfoService;

    GitLoginService gitLoginService;

    public final String CLIENT_ID = "d9f9e0e5413419ab273e";

    public final String CLIENT_SECRET = "ba1e86d41d2382078aea528d7c7410dc560e128b";

    public final String URL = "https://challenge.adalab.cn/callback";

    public GitLoginController(StudentInfoService studentInfoService,GitLoginService gitLoginService) {
        this.studentInfoService = studentInfoService;
        this.gitLoginService = gitLoginService;
    }


    @SneakyThrows
    @GetMapping("/callback")
    public void getAccessToken(@RequestParam(name = "code") String code,
                               @RequestParam(name = "state") String state,
                               HttpServletRequest request,
                               HttpServletResponse resp) {
        String token = gitLoginService.callBack(CLIENT_ID, CLIENT_SECRET,URL,code,state);
        // 如果token为null，那么与gihub的连结出了问题，重定向回login界面
        //不为null，获得用户并重定向到闯关界面
        if (token == null) {
            Cookie cookie = new Cookie("NETERROR","NETERROR");
            resp.addCookie(cookie);
            resp.sendRedirect("https://challenge.adalab.cn/student");

        } else {
            StudentInfo student = gitLoginService.getUser(token);
            Cookie cookie = new Cookie("id",student.getId()+"");
            resp.addCookie(cookie);
            resp.sendRedirect("https://challenge.adalab.cn/home");
        }
    }
}
