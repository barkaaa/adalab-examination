package com.adalab.examination.controller;


import com.adalab.examination.entity.Student;
import com.adalab.examination.entity.StudentInfo;
import com.adalab.examination.service.StudentInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Karl
 * @since 2022-08-27
 */
@RestController
@RequestMapping("/api/examination/student-info")
public class StudentInfoController {

    final
    StudentInfoService studentInfoService;

    public StudentInfoController(StudentInfoService studentInfoService) {
        this.studentInfoService = studentInfoService;
    }

    /** zxear专用
     * 获取单个 student 的所有信息
     * @param studentInfo 学生的姓名
     * @return 学生信息
     */
    @PostMapping("/getDetail")
    public HashMap<Object, Object> getDetail(@NotNull @RequestBody StudentInfo studentInfo) {
        LambdaQueryWrapper<StudentInfo> studentQuery = new LambdaQueryWrapper<>();
        studentQuery.eq(StudentInfo::getName, studentInfo.getName());
        StudentInfo one = studentInfoService.getOne(studentQuery);
        HashMap<Object, Object> objectHashMap = new HashMap<>();
        objectHashMap.put("name",one.getName());
        objectHashMap.put("Date Created",one.getCreatedDate());
        objectHashMap.put("Days needed",one.getDaysNeeded());
        objectHashMap.put("Actual Days",one.getActualDays());
        objectHashMap.put("Last Edited",one.getLastEdited());
        objectHashMap.put("Current Week",one.getId());
        objectHashMap.put("type",one.getType());
        return objectHashMap;
    }

    /**
     * @return 所有学生数据组成的LIST
     */
    @GetMapping("getList")
    public List<StudentInfo> getList(){
        LambdaQueryWrapper<StudentInfo> studentInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        studentInfoLambdaQueryWrapper.orderByDesc(StudentInfo::getRanking);
        return studentInfoService.list(studentInfoLambdaQueryWrapper);
    }
}

