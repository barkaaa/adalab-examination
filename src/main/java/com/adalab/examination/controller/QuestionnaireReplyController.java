package com.adalab.examination.controller;


import com.adalab.examination.entity.QuestionnaireReply;
import com.adalab.examination.entity.ReplyInfo;
import com.adalab.examination.entity.ServiceResponse;
import com.adalab.examination.entity.StudentInfo;
import com.adalab.examination.entity.missionEntity.QuestionnaireResult;
import com.adalab.examination.service.QuestionnaireReplyService;
import com.adalab.examination.service.StudentInfoService;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Karl
 * @since 2022-08-31
 */
@RestController
@RequestMapping("/api/reply")
@CrossOrigin
public class QuestionnaireReplyController {
    QuestionnaireReplyService questionnaireReplyService;
    StudentInfoService studentInfoService;

    QuestionnaireReplyController(QuestionnaireReplyService questionnaireReplyService,StudentInfoService studentInfoService){
        this.questionnaireReplyService = questionnaireReplyService;
        this.studentInfoService = studentInfoService;
    }

    /**
     * 上传学生的问卷回答到数据库
     * @param q
     * @return
     */
    @PutMapping("putReply")
    public ServiceResponse<String> putReply(@RequestBody QuestionnaireResult q){
        questionnaireReplyService.putStudentReply(q);
        return new ServiceResponse<>(200,"提交成功");
    }

    /**
     * 得到所有学员对问卷的回答
     * @return
     */
    @GetMapping("getAllReply")
    public ServiceResponse<List<ReplyInfo>> getAllReply(){
        return new ServiceResponse<>(200,"success",questionnaireReplyService.getReply());
    }

}

