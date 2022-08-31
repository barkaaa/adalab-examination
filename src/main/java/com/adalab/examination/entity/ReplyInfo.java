package com.adalab.examination.entity;

import lombok.Data;

import java.util.List;

/**
 * 获得学员提交的问卷答案
 */
@Data
public class ReplyInfo {

    private int studentId;

    private String studentName;

    private List<List<String>> replyList;
}
