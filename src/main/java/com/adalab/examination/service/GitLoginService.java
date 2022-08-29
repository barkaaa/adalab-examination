package com.adalab.examination.service;

import com.adalab.examination.entity.AccessTokenDTO;
import com.adalab.examination.entity.GitHubUser;
import com.adalab.examination.entity.StudentInfo;
import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class GitLoginService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    StudentInfoService studentInfoService;

    GitLoginService(StudentInfoService studentInfoService){
        this.studentInfoService = studentInfoService;
    }

    public String callBack(String clientId, String clientSecret, String url, String code, String state) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(url);
        accessTokenDTO.setState(state);
        //进行doPost请求，获取access_token
        String token = getAccessToken(accessTokenDTO);
        return token;

    }
    //第二次发送请求到github，并得到github返回token
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            //向github发送授权请求，最容易因为网络问题导致出错
            String string = response.body().string();
            logger.info("token:" + string);
            String[] split = string.split("&");
            return split[0].split("=")[1];
        } catch (IOException e) {
            logger.error("获取githubToken时网络出错");
        }
        return null;
    }

    public StudentInfo getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .header("Authorization", "token " + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            System.out.println("==============");
            String string = response.body().string();
            System.out.println(string);
            System.out.println("==============");
            GitHubUser gitHubUser = JSON.parseObject(string, GitHubUser.class);//将string解析成GitHub对象
            logger.info(gitHubUser + "");
            //如果用户id等于0证明没有成功拿到用户信息
            if (gitHubUser.getId() == 0) {
                logger.error("没有拿到用户信息");
                return null;
            } else {

                StudentInfo student = studentInfoService.getById(gitHubUser.getId());
                //student为null，新用户，需要上传数据库，老用户直接返回
                return Objects.requireNonNullElseGet(student, () -> uploadDatabase(gitHubUser));
            }
        } catch (IOException e) {
            logger.error("getUser Error");
            return null;
        }
    }

    private StudentInfo uploadDatabase(GitHubUser gitHubUser) {
        String name = gitHubUser.getLogin();
        int id = gitHubUser.getId();
        String email = gitHubUser.getEmail();
        String avatar = gitHubUser.getAvatar_url();

        StudentInfo student = new StudentInfo();
        student.setId(id);
        student.setName(name);
        student.setEmail(email);
        student.setAvatar(avatar);
        student.setEpisode(0);
        student.setCreatedDate(LocalDateTime.now());

        studentInfoService.save(student);
        return student;
    }

}
