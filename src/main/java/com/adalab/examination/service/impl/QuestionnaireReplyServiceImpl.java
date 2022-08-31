package com.adalab.examination.service.impl;

import com.adalab.examination.entity.QuestionnaireReply;
import com.adalab.examination.entity.missionEntity.QuestionnaireResult;
import com.adalab.examination.mapper.QuestionnaireReplyMapper;
import com.adalab.examination.service.QuestionnaireReplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Karl
 * @since 2022-08-31
 */
@Service
public class QuestionnaireReplyServiceImpl extends ServiceImpl<QuestionnaireReplyMapper, QuestionnaireReply> implements QuestionnaireReplyService {



    @Override
    public void putStudentReply(QuestionnaireResult q) {
        QuestionnaireReply questionnaireReply = new QuestionnaireReply();
        questionnaireReply.setMissionId(q.getCurrentMission());
        questionnaireReply.setStudentId(q.getCurrentStudent());
        AtomicInteger i = new AtomicInteger(1);
        q.getList().forEach(item -> {
            StringBuilder sb = new StringBuilder();
            String fill = item.getFill();
            if (!"".equals(fill)) {
                sb.append(item.getFill()).append("%");
            }
            if (item.getSelectOptions() != null && item.getSelectOptions().size() != 0) {
                for (String s : item.getSelectOptions()) {
                    sb.append(s).append("%");
                }
            }

            questionnaireReply.setReply(sb.toString());
            questionnaireReply.setQuestionId(i.get());
            save(questionnaireReply);
            i.getAndIncrement();
        });
    }
}
