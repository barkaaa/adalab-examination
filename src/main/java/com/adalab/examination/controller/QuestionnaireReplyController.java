package com.adalab.examination.controller;


import com.adalab.examination.entity.Questionnaire;
import com.adalab.examination.entity.QuestionnaireReply;
import com.adalab.examination.entity.ServiceResponse;
import com.adalab.examination.entity.StudentInfo;
import com.adalab.examination.entity.missionEntity.QuestionnaireResult;
import com.adalab.examination.service.QuestionnaireReplyService;
import com.adalab.examination.service.QuestionnaireService;
import com.adalab.examination.service.StudentInfoService;
import org.springframework.web.bind.annotation.*;

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
    @PutMapping("putReply")
    public ServiceResponse putReply(@RequestBody QuestionnaireResult q){
        QuestionnaireReply questionnaireReply = new QuestionnaireReply();
        questionnaireReply.setMissionId(q.getCurrentMission());
        questionnaireReply.setStudentId(q.getCurrentStudent());
        q.getList().stream().forEach(item->{
            StringBuilder sb = new StringBuilder();
            sb.append(item.getFill()).append("%");
            for(String s : item.getSelectOptions()){
                sb.append(s).append("%");
            }
            questionnaireReply.setReply(sb.toString());
            questionnaireReplyService.save(questionnaireReply);
        });
        return new ServiceResponse<>(200,"提交成功");
    }
}

