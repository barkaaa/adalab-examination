package com.adalab.examination.controller;


import com.adalab.examination.entity.ServiceResponse;
import com.adalab.examination.entity.StudentInfo;
import com.adalab.examination.service.StudentInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static com.adalab.examination.GitClone.DirectoryUtils.traverseDir;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Karl
 * @since 2022-08-27
 */
@RestController
@RequestMapping("/api/studentInfo")
public class StudentInfoController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    final
    StudentInfoService studentInfoService;

    public StudentInfoController(StudentInfoService studentInfoService) {
        this.studentInfoService = studentInfoService;
    }


    //获取学员代码提交历史记录的文件结构树 精确到关卡
    @PostMapping("/studentCode/FilesTree/{name}")
    public ServiceResponse<HashMap<String, Object>> getFilesTree(@RequestBody Map<String, String> map, @PathVariable String name) {
        String userName = "/" + name;
        String step = "/step" + map.get("step");
        String localPath = System.getProperty("user.dir") + "/src/main/resources/studentCode" + userName + step;
        HashMap<String, Object> hashMap = traverseDir(localPath);
        logger.info("获取到结构树");
        return new ServiceResponse<>(200, "", hashMap);
    }

    /**
     * 闯关结束后则调用这个接口
     * 设置实际所用了多少小时
     *
     * @param id
     * @return
     */
    @GetMapping("success/{id}")
    public ServiceResponse<String> end(@PathVariable int id) {
        StudentInfo studentInfo = studentInfoService.getById(id);
        int actual = (int) Duration.between(studentInfo.getBeginDate(), LocalDateTime.now()).toHours();
        studentInfo.setActualHours(actual);
        if (studentInfoService.save(studentInfo)) {
            return new ServiceResponse<>(200, "ok", "");
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 在前端按下开始按钮开始闯关
     * 设置学员开始时间
     *
     * @param id
     * @return
     */
    @GetMapping("begin/{id}")
    public ServiceResponse<String> begin(@PathVariable int id) {
        StudentInfo studentInfo = studentInfoService.getById(id);
        studentInfo.setBeginDate(LocalDateTime.now());
        if (studentInfoService.updateById(studentInfo)) {
            return new ServiceResponse<String>(200, "SUCCESS");
        } else {
            return new ServiceResponse<String>(201, "未能成功设置开始时间");
        }
    }

    /**
     * 通过id得到学生信息
     *
     * @param id cookie存的是string类型的ID，这里也按照string处理
     * @return 学生信息
     */
    @GetMapping("getStudent/{id}")
    public ServiceResponse<StudentInfo> getStudentById(@PathVariable String id) {
        int studentId = Integer.parseInt(id);
        LambdaQueryWrapper<StudentInfo> lqw = new LambdaQueryWrapper<>();
        StudentInfo studentInfo = studentInfoService.getById(studentId);
        if (studentInfo != null) {
            return new ServiceResponse<>(200, "success", studentInfo);
        } else {
            return new ServiceResponse<>(201, "未能成功获取学员信息");
        }
    }


    /**
     * 获取单个 student 的所有信息
     *
     * @param studentInfo 学生的姓名
     * @return 学生信息
     */
    @PostMapping("/getDetail")
    public ServiceResponse<HashMap<Object, Object>> getDetail(@NotNull @RequestBody StudentInfo studentInfo) {
        LambdaQueryWrapper<StudentInfo> studentQuery = new LambdaQueryWrapper<>();
        studentQuery.eq(StudentInfo::getName, studentInfo.getName());
        StudentInfo one = studentInfoService.getOne(studentQuery);
        HashMap<Object, Object> objectHashMap = new HashMap<>();
        objectHashMap.put("name", one.getName());
        objectHashMap.put("Date Created", one.getCreatedDate());
        objectHashMap.put("Days needed", one.getDaysNeeded());
        objectHashMap.put("Actual Hours", one.getActualHours());
        objectHashMap.put("Last Edited", one.getLastEdited());
        objectHashMap.put("Current Week", one.getId());
        objectHashMap.put("type", one.getType());
        return new ServiceResponse<>(200, "", objectHashMap);
    }

    /**
     * @return 所有学生数据组成的LIST
     */
    @GetMapping("getList")
    public ServiceResponse<List<StudentInfo>> getList() {
        LambdaQueryWrapper<StudentInfo> studentInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        studentInfoLambdaQueryWrapper.Desc(StudentInfo::getRanking);
        return new ServiceResponse<>(200, "", studentInfoService.list(studentInfoLambdaQueryWrapper));
    }

    @GetMapping("getRanking")
    public ServiceResponse<List<StudentInfo>> getRanking() {
        LambdaQueryWrapper<StudentInfo> studentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        studentLambdaQueryWrapper.orderByDesc(StudentInfo::getEpisode);
        return new ServiceResponse<>(200, "", studentInfoService.list(studentLambdaQueryWrapper));
    }


    //获取总页数
    @GetMapping("/getTotalPages/{piecesNum}")
    public ServiceResponse<Long> getTotalPages(@PathVariable int piecesNum) {
        long toalPieces = studentInfoService.count();
        long pageNum;
        if (toalPieces % piecesNum == 0) {
            pageNum = toalPieces / piecesNum;
        } else {
            pageNum = toalPieces / piecesNum + 1;
        }
        return new ServiceResponse<>(200, "success", pageNum);
    }


    //分页获取排名
    @GetMapping("getPagingRanking/{page}")
    public ServiceResponse<List<StudentInfo>> getPagingRanking(@PathVariable int page) {
        IPage<StudentInfo> pageParameter = new Page<>(page, 14);

        LambdaQueryWrapper<StudentInfo> studentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        studentLambdaQueryWrapper.orderByDesc(StudentInfo::getEpisode);
        IPage<StudentInfo> toolPage = studentInfoService.page(pageParameter, studentLambdaQueryWrapper);
        return new ServiceResponse<>(200, "success", toolPage.getRecords());
    }

    //获取提交信息表格行
    @GetMapping("/getSubmission/{name}")
    public ServiceResponse<List<Map<String, String>>> getSubmission(@PathVariable String name) {
        LambdaQueryWrapper<StudentInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StudentInfo::getName,name);
        int episopde = studentInfoService.getOne(queryWrapper).getEpisode();
        String userName = "/" + name;
        String localPath = "src/main/resources/studentCode" + userName;
//        String localPath ="src/main/resources/studentCode";
        List<Map<String, String>> form = new ArrayList<>();
        HashMap<String, Object> hashMap = traverseDir(localPath);
        File file = new File(localPath);
        if (file.exists()) {
            String[] stepList = file.list();
            ArrayUtils.reverse(stepList);
            for (String step : stepList) {
                String innerPath = localPath + "/" + step;
                String[] timeList = new File(innerPath).list();
                ArrayUtils.reverse(timeList);
                for (String time : timeList) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formatedTime = simpleDateFormat.format(new Date(Long.parseLong(time) * 1000L));
                    Map<String, String> map = new HashMap<>();
                    map.put("commitTime", formatedTime);
                    map.put("link", step);
                    map.put("episode", step);
                    map.put("src", innerPath);
                    form.add(map);
                    System.out.println("list:" + step + formatedTime);

                }
            }
        }

        return new ServiceResponse<>(200, "", form);
    }
    @GetMapping("/studentCode/FilesTree")
    public Map<String,Map<String,List<String>>> getFilesTreeAll() {
        String localPath = "src/main/resources/studentCode";
        HashMap<String, Object> hashMap = traverseDir(localPath);
        //获取用户提交文件夹名
        File file  = new File(localPath);
        String[] usrFolderName = file.list();
        Map<String,Map<String,List<String>>> usrFolderNames = new HashMap<>();
        for (String U:usrFolderName){
            String innerpath = localPath+"/"+U;
            //获取日期文件夹 名
            File innerFile = new File(innerpath);
            String[] dateFolder = innerFile.list();
            Map<String,List<String>> dataFolderNames = new HashMap<>();
            for (String D:dateFolder){
                String innerInerPath = innerpath+"/"+D;
                //获取文件名
                File innerInnerFile = new File(innerpath);
                String[] files = innerInnerFile.list();
                List<String> fileNames = new ArrayList<>();
                for (String F: files){
                    fileNames.add(F);
                }
                dataFolderNames.put(D,fileNames);
            }
            usrFolderNames.put(U,dataFolderNames);
        }
        logger.info("获取到结构树");
        System.out.println(hashMap.get("佐々木玲奈"));
        return usrFolderNames;
    }

    //设置学生的闯关数
    @GetMapping("/setDoneMission/{id}")
    public ServiceResponse<String> setDoneMission(@PathVariable String id) {
        int studentId = Integer.parseInt(id);
        LambdaQueryWrapper<StudentInfo> lqw = new LambdaQueryWrapper<>();
        StudentInfo studentInfo = studentInfoService.getById(studentId);
        int doneMission = studentInfo.getEpisode();
        studentInfo.setEpisode(doneMission + 1);
        studentInfoService.saveOrUpdate(studentInfo);
        return new ServiceResponse<>(200, "success");
    }
}

