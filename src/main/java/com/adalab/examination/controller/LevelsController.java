package com.adalab.examination.controller;


import com.adalab.examination.entity.Levels;
import com.adalab.examination.mapper.LevelsMapper;
import com.adalab.examination.service.LevelsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/levels")
public class LevelsController {
    final
    LevelsMapper levelsMapper;
    final LevelsService levelsService;

    public LevelsController(LevelsMapper levelsMapper, LevelsService levelsService) {
        this.levelsMapper = levelsMapper;
        this.levelsService = levelsService;
    }


    @GetMapping("/all")
    public List<Levels> getAll() {
        return levelsMapper.selectList(null);
    }


    @PostMapping("/add")
    public String add(@RequestBody Levels levels) {

        levelsService.save(levels);
        return "添加成功";
    }
}
