package com.adalab.examination.service;

import com.adalab.examination.entity.QuestionnaireReply;
import com.adalab.examination.entity.missionEntity.QuestionnaireResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Karl
 * @since 2022-08-31
 */
public interface QuestionnaireReplyService extends IService<QuestionnaireReply> {

    void putStudentReply(QuestionnaireResult q);
}
