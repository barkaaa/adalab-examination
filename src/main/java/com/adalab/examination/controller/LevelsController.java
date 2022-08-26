package com.adalab.examination.controller;


import com.adalab.examination.entity.Levels;
import com.adalab.examination.mapper.LevelsMapper;
import com.adalab.examination.service.LevelsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/levels")
public class LevelsController {
    @Autowired
    LevelsMapper levelsMapper;


    @Autowired
    LevelsService levelsService;


    @GetMapping("/all")
    public List<Levels> getAll() {
        List<Levels> levels = levelsMapper.selectList(null);
        return levels;
    }


    @PostMapping("/add")
    public String add(@RequestBody Levels levels){

        levelsService.save(levels);
        return "添加成功";
    }
}
