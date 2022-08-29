package com.adalab.examination.controller;


import com.adalab.examination.entity.ChallengeType;
import com.adalab.examination.service.ChallengeTypeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Karl
 * @since 2022-08-29
 */
@RestController
@RequestMapping("/api/challenge-type")
public class ChallengeTypeController {


    @Autowired
    ChallengeTypeService challengeTypeService;

    @PostMapping("/add")
    public String add(@RequestBody ChallengeType challengeType){

        challengeTypeService.save(challengeType);
        return "添加成功";
    }
    @GetMapping("/getone")
    public ChallengeType getOne(int stage) {
        return challengeTypeService.getOne(new QueryWrapper<ChallengeType>().eq("stage", stage));
    }
    @GetMapping("/all")
    public List<ChallengeType> getAll(){
        List<ChallengeType> list = challengeTypeService.list(null);
        return list;
    }
}

