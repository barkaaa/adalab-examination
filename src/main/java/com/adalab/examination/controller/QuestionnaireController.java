package com.adalab.examination.controller;


import com.adalab.examination.entity.Questionnaire;
import com.adalab.examination.entity.ServiceResponse;
import com.adalab.examination.entity.missionEntity.MissionInfo;
import com.adalab.examination.entity.missionEntity.TextContents;
import com.adalab.examination.service.QuestionnaireService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
        questionnaireService.addOrUpdateMission(missionInfo);

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
        questionnaireService.deleteById(missionNumber);
        return new ServiceResponse<>(200, "ok");
    }

}
