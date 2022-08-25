package com.adalab.examination.entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TestResult {
    boolean passed;

    Map<String, String> mes;

    public TestResult() {
        mes = new HashMap<>();
    }

    public TestResult(File file) {
        mes = new HashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            mes = objectMapper.readValue(file, new TypeReference<>() {
            });
            passed = mes.get("result").equals("true");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            this.passed = false;
            mes.put("errMessage", "测试结果丢失|测试结果文件生成错误");
        }
    }

    public Map<String, String> getMes() {
        return mes;
    }

    public boolean isPassed() {
        return passed;
    }
}
