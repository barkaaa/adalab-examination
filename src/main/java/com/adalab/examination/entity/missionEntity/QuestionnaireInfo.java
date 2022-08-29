package com.adalab.examination.entity.missionEntity;

import lombok.Data;

import java.util.List;

@Data
public class QuestionnaireInfo {

    private String fill;
    private List<String> selectOptions;
}
