package com.adalab.examination.controller;


import com.adalab.examination.entity.Student;
import com.adalab.examination.service.StudentService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.TransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.adalab.examination.GitClone.GitUtils.delAllFile;

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
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    StudentService studentService;

    @GetMapping("/ping")
    public String ping(){
        return "pong";
    }

    /**
     *
     * @return 所有学生数据组成的LIST
     */
    @GetMapping("getRanking")
    public List<Student> getRanking(){
        return studentService.list();
    }

    /**根据需求会有一个闯关页面收集学员更详细信息
     *
     * @param student
     * @return
     */
    @PostMapping("updateStudent")
    public String update(@RequestBody Student student){
        if(studentService.updateById(student)){
            return "success";
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    //在resources文件夹下根据学员姓名生成文件夹 clone学员的代码
    @PostMapping("/studentCode")
    public String cloneCode(@RequestBody Map<String,String>map){
        String gitURL = map.get("url");
        String path = System.getProperty("user.dir");
        String name = "/"+map.get("name");
        String pattern = "MM-dd-HH:mm:ss";
        //获取当地时区
        Locale locale = Locale.getDefault();
        //利用SimpleDateFormat 进行时间格式的转换
        SimpleDateFormat sdf = new SimpleDateFormat(pattern,locale);
        String time = "/"+sdf.format(new Date());

        String localPath = path+"/src/main/resources/studentCode"+name+time;//+studentName
        File file = new File(localPath);
        if (file.exists()){
            logger.info("删除文件结果:"+delAllFile(localPath));
        }

        try {
            Git git = Git.cloneRepository()
                    .setURI(gitURL)
                    .setDirectory(new File(localPath))
                    .call();
            logger.info("clone成功!"+git.toString());
        } catch (GitAPIException e) {
            logger.error("clone失败!");
            e.printStackTrace();
            return "克隆失败";
        }
        return "OK";
    }

}

