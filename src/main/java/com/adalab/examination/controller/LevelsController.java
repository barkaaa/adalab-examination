package com.adalab.examination.controller;


import com.adalab.examination.entity.Levels;
import com.adalab.examination.mapper.LevelsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/levels")
public class LevelsController {
    @Autowired
    LevelsMapper levelsMapper;


    @GetMapping("/all")
    public List<Levels> getAll() {
        List<Levels> levels = levelsMapper.selectList(null);
        return levels;
    }
}
