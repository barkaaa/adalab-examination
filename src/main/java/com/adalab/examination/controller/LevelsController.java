package com.adalab.examination.controller;


import com.adalab.examination.entity.Levels;
import com.adalab.examination.service.LevelsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/levels")
public class LevelsController {


    @Autowired
    LevelsService levelsService;


    @GetMapping("/all")
    public List<Levels> getAll() {
        return levelsService.list(null);
    }

    @GetMapping("/getone")
    public Levels getOne(int stage) {
        return levelsService.getOne(new QueryWrapper<Levels>().eq("stage", stage));
    }


    @PostMapping("/add")
    public String add(@RequestBody Levels levels) {
        levelsService.save(levels);
        return "添加成功";
    }

    @PutMapping("/update")
    public void update(@RequestBody Levels levels) {
        levelsService.updateById(levels);
    }


    @DeleteMapping("/batchdel")
    public void batchDel(@RequestBody Map<String, List<Integer>>map) {
        levelsService.removeByIds(map.get("ids"));
    }
}
