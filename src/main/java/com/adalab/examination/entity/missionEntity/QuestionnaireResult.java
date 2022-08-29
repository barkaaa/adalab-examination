package com.adalab.examination.entity.missionEntity;

import lombok.Data;

import java.util.List;

@Data
public class QuestionnaireResult {

    private List<QuestionnaireInfo> list;

    private int currentMission;

    private int currentStudent;
}
