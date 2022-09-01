package com.adalab.examination.service;

import com.adalab.examination.entity.Questionnaire;
import com.adalab.examination.entity.missionEntity.MissionInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Karl
 * @since 2022-08-27
 */
public interface QuestionnaireService extends IService<Questionnaire> {

    void addOrUpdateMission(MissionInfo missionInfo);

    void deleteById(int missionNumber);
}
