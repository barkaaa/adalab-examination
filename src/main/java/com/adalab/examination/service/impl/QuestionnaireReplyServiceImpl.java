package com.adalab.examination.service.impl;

import com.adalab.examination.entity.QuestionnaireReply;
import com.adalab.examination.entity.ReplyInfo;
import com.adalab.examination.entity.missionEntity.QuestionnaireResult;
import com.adalab.examination.mapper.QuestionnaireReplyMapper;
import com.adalab.examination.service.QuestionnaireReplyService;
import com.adalab.examination.service.StudentInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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

    final
    StudentInfoService studentInfoService;

    public QuestionnaireReplyServiceImpl(StudentInfoService studentInfoService) {
        this.studentInfoService = studentInfoService;
    }


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

    @Override
    public List<ReplyInfo> getReply() {
        //数据准备
        var list = list();
        Map<Integer,List<List<String>>> map = new HashMap<>();
        List<ReplyInfo> returnList = new ArrayList<>();
        //遍历获得的每一条数据
        list.stream().forEach(item ->{
            //按学生分组，已有数据只需添加reply的list到更大的list
            if(map.containsKey(item.getStudentId())){
                String reply = item.getReply();
                var split = reply.split("%");
                List<String> replyList = new ArrayList<>(Arrays.asList(split));
                map.get(item.getStudentId()).add(replyList);
            }else{
                //新的数据需要添加到map中，以ID为key
                List<List<String>> replyList = new ArrayList<>();
                String reply = item.getReply();
                var split = reply.split("%");
                List<String> list1 = new ArrayList<>(Arrays.asList(split));
                replyList.add(list1);
                map.put(item.getStudentId(),replyList);
            }
        });
        //遍历完list开始遍历map准备返回数据
        for(int i : map.keySet()){
            ReplyInfo replyInfo = new ReplyInfo();

            var student = studentInfoService.getById(i);
            replyInfo.setStudentId(student.getId());
            replyInfo.setStudentName(student.getName());
            replyInfo.setReplyList(map.get(student.getId()));
            returnList.add(replyInfo);
        }
        return returnList;
    }
}
