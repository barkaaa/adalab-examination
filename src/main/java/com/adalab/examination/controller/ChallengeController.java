package com.adalab.examination.controller;


import com.adalab.examination.entity.Challenge;
import com.adalab.examination.service.ChallengeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Karl
 * @since 2022-08-29
 */
@RestController
@RequestMapping("/api/challenge")
public class ChallengeController {


    @Autowired
    ChallengeService challengeService;
    @GetMapping("/all")
    public List<Challenge> getAll() {
        List<Challenge> list = challengeService.list(null);
        return list;
    }

    @GetMapping("/getone")
    public Challenge getOne(int stage) {
        Challenge one = challengeService.getOne(new QueryWrapper<Challenge>().eq("stage", stage));
        return one;
    }


    @PostMapping("/add")
    public String add(@RequestBody Challenge challenge) {
        challengeService.saveWithType(challenge);
        return "添加成功";
    }

    @PutMapping("/update")
    public void update(@RequestBody Challenge challenge) {
        challengeService.updateById(challenge);
    }

}

