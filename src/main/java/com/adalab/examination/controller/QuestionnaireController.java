package com.adalab.examination.controller;


import com.adalab.examination.entity.Questionnaire;
import com.adalab.examination.entity.missionEntity.MissionInfo;
import com.adalab.examination.entity.missionEntity.TextContents;
import com.adalab.examination.service.QuestionnaireService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/api/questionnaire")
@CrossOrigin
public class QuestionnaireController {

    final
    QuestionnaireService questionnaireService;

    public QuestionnaireController(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    @PutMapping("addorupdate")
    public String addOrUpdate(@RequestBody MissionInfo missionInfo) {
        Questionnaire questionnaire = new Questionnaire();
        //第几关
        questionnaire.setMissionNumber(missionInfo.getMissionNumber());

        int i = 1;
        for (TextContents textContents : missionInfo.getTextContents()) {

            if (textContents.getQuestionType().equals("选择")) {
                questionnaire.setQuestionType(2);
            } else {
                questionnaire.setQuestionType(1);
            }

            questionnaire.setTheme(textContents.getDescription());
            questionnaire.setQuestionNumber(i);
            //将会得到“false”或“true”
            questionnaire.setIsAddtional(textContents.getIsAdditional());
            questionnaire.setIsMultiple(textContents.getIsMultiple());

            StringBuilder sb = new StringBuilder();
            for (String str : textContents.getOptions()) {
                sb.append(str).append("?");
            }
            questionnaire.setOptions(sb.toString());

            QueryWrapper<Questionnaire> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("mission_number", questionnaire.getMissionNumber());
            queryWrapper.eq("question_number", i);
            Questionnaire query = questionnaireService.getOne(queryWrapper);

            if (query == null) {
                questionnaireService.save(questionnaire);
            } else {
                questionnaireService.update(questionnaire, queryWrapper);
            }
            i++;
        }

        return "ok";
    }


    @GetMapping("getone")
    public List<Questionnaire> getOne(int missionNum) {
        QueryWrapper<Questionnaire> qw = new QueryWrapper<Questionnaire>().eq("mission_number", missionNum);
        return questionnaireService.list(qw);
    }


}
