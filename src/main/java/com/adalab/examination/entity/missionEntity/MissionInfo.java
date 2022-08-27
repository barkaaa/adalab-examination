package com.adalab.examination.entity.missionEntity;

import lombok.Data;

import java.util.List;

@Data
public class MissionInfo {

    private int missionNumber;

    private String type;

    private List<TextContents> textContents;
}
