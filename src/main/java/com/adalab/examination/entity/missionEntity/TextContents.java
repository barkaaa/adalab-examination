package com.adalab.examination.entity.missionEntity;

import lombok.Data;

import java.util.List;

@Data
public class TextContents {

    private String questionType;

    private String description;

    private List<String> options;

    private String isMultiple;

    private String isAdditional;
}
