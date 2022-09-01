package com.adalab.examination.service.impl;

import com.adalab.examination.entity.Questionnaire;
import com.adalab.examination.entity.missionEntity.MissionInfo;
import com.adalab.examination.entity.missionEntity.TextContents;
import com.adalab.examination.mapper.QuestionnaireMapper;
import com.adalab.examination.service.QuestionnaireService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Karl
 * @since 2022-08-27
 */
@Service
public class QuestionnaireServiceImpl extends ServiceImpl<QuestionnaireMapper, Questionnaire> implements QuestionnaireService {

    @Override
    public void addOrUpdateMission(MissionInfo missionInfo) {
        Questionnaire questionnaire = new Questionnaire();
        //第几关
        questionnaire.setMissionNumber(missionInfo.getMissionNumber());

        int i = 1;
        for (TextContents textContents : missionInfo.getTextContents()) {
            if ("选择".equals(textContents.getQuestionType())) {
                questionnaire.setQuestionType(2);
            } else {
                questionnaire.setQuestionType(1);
            }
            //设置题干
            questionnaire.setTheme(textContents.getDescription());
            //设置第几关的第几题
            questionnaire.setQuestionNumber(i);
            //将会得到“false”或“true”
            questionnaire.setIsAddtional(textContents.getIsAdditional());
            questionnaire.setIsMultiple(textContents.getIsMultiple());
            //将选项保存进去，每个选项用？分割
            StringBuilder sb = new StringBuilder();
            for (String str : textContents.getOptions()) {
                if(!"".equals(str)) {
                    sb.append(str).append("?");
                }
            }
            questionnaire.setOptions(sb.toString());
            //问题id和题目id都能对应表明该问题存在，更新，反正新增
            QueryWrapper<Questionnaire> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("mission_number", questionnaire.getMissionNumber());
            queryWrapper.eq("question_number", i);
            Questionnaire query = getOne(queryWrapper);

            if (query == null) {
                save(questionnaire);
            } else {
                update(questionnaire, queryWrapper);
            }
            i++;
        }
    }

    @Override
    public void deleteById(int missionNumber) {
        LambdaQueryWrapper<Questionnaire> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Questionnaire::getMissionNumber, missionNumber);
        var list = list(lqw);
        list.stream().forEach(this::removeById);
        lqw.clear();
        lqw.gt(Questionnaire::getMissionNumber, missionNumber);
        var list2 = list(lqw);
        list2.stream().forEach((item) -> {
                    item.setMissionNumber(item.getMissionNumber() - 1);
                    updateById(item);
                }
        );
    }
}
