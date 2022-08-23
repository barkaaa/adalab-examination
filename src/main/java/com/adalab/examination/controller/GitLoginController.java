package com.adalab.examination.controller;


import com.adalab.examination.entity.AccessTokenDTO;
import com.adalab.examination.entity.Student;
import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;

@RestController("/api/git")
public class GitLoginController {


    public final String CLIENTID = "d9f9e0e5413419ab273e";

    public final String CLIENTSECRET = "ba1e86d41d2382078aea528d7c7410dc560e128b";

    public final String URL = "http://localhost:8080/callback";

    @GetMapping("sendLogin")
    public void sendLogin(HttpSession session) {

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
        System.out.println(token);
        Student student = getUser(token);

        return student;
    }

    public Student getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url("https://api.github.com/user?access_token="+ accessToken)
//                .build();
        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .header("Authorization","token "+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            System.out.println("string2"+string);

            Student student = JSON.parseObject(string, Student.class);//将string解析成GitHub对象

            return student;
        } catch (IOException e) {
            return null;
        }

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
            System.out.println("string:"+string);
            String[] split = string.split("&");
            String token = split[0].split("=")[1];
            return token;
        } catch (IOException e) {

        }
        return null;
    }


}
