package com.adalab.examination.controller;


import com.adalab.examination.entity.Questionnaire;
import com.adalab.examination.entity.ServiceResponse;
import com.adalab.examination.entity.missionEntity.MissionInfo;
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
    public ServiceResponse<String> addOrUpdate(@RequestBody MissionInfo missionInfo) {
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

        return new ServiceResponse<>(200, "ok");
    }


    @GetMapping("getone")
    public ServiceResponse<List<Questionnaire>> getOne(int missionNum) {
        QueryWrapper<Questionnaire> qw = new QueryWrapper<Questionnaire>().eq("mission_number", missionNum);
        return new ServiceResponse<>(200, "", questionnaireService.list(qw));
    }

    /**
     * 前端删除关卡调用此接口，先删除关卡下所有问题，然后将后面问题的missionnumber前移一位
     *
     * @param missionNumber 该问题属于第几关
     * @return
     */
    @DeleteMapping("DeleteQuestionnaire/{missionNumber}")
    public ServiceResponse<String> deleteQuestionnaire(@PathVariable int missionNumber) {
        LambdaQueryWrapper<Questionnaire> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Questionnaire::getMissionNumber, missionNumber);
        var list = questionnaireService.list(lqw);
        list.stream().forEach(questionnaireService::removeById);
        lqw.clear();
        lqw.gt(Questionnaire::getMissionNumber, missionNumber);
        var list2 = questionnaireService.list(lqw);
        list2.stream().forEach((item) -> {
                    item.setMissionNumber(item.getMissionNumber() - 1);
                    questionnaireService.updateById(item);
                }

        );
        return new ServiceResponse<>(200, "ok");
    }

}
