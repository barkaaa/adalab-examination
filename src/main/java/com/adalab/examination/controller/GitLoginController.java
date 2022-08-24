package com.adalab.examination.controller;


import com.adalab.examination.entity.AccessTokenDTO;
import com.adalab.examination.entity.GitHubUser;
import com.adalab.examination.entity.Student;
import com.adalab.examination.service.StudentService;
import com.alibaba.fastjson.JSON;
import okhttp3.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController("/api/git")
public class GitLoginController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    StudentService studentService;

    public final String CLIENTID = "d9f9e0e5413419ab273e";

    public final String CLIENTSECRET = "ba1e86d41d2382078aea528d7c7410dc560e128b";

    public final String URL = "http://localhost:8080/callback";

    @GetMapping("sendLogin")
    public void sendLogin(HttpSession session) {
        //TODO
    }
    @GetMapping("/callback")
    public Student getAccessToken(@RequestParam(name="code") String code,
                                 @RequestParam(name = "state") String state) {

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(CLIENTID);
        accessTokenDTO.setClient_secret(CLIENTSECRET);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(URL);
        accessTokenDTO.setState(state);
        //进行doPost请求，获取access_token
        String token = getAccessToken(accessTokenDTO);
        Student student = getUser(token);

        return student;
    }

    public Student getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .header("Authorization","token "+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            logger.info(string);
            GitHubUser gitHubUser = JSON.parseObject(string, GitHubUser.class);//将string解析成GitHub对象
            Student student = uploadDatabase(gitHubUser);
            return student;
        } catch (IOException e) {
            return null;
        }

    }
    private Student uploadDatabase(GitHubUser gitHubUser){
        String name = gitHubUser.getLogin();
        int id = gitHubUser.getId();
        String email = gitHubUser.getEmail();
        String avatar = gitHubUser.getAvatar_url();
        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setEmail(email);
        student.setAvatar(avatar);
        student.setRanking(0);
        studentService.save(student);
        return student;
    }

    //第二次发送请求到github，并得到github返回token
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType= MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            logger.info("token:"+string);
            String[] split = string.split("&");
            String token = split[0].split("=")[1];
            return token;
        } catch (IOException e) {
            logger.error("获取githubToken部分出错");
        }
        return null;
    }


}
