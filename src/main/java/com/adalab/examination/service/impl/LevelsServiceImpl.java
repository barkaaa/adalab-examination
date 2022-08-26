package com.adalab.examination.service.impl;

import com.adalab.examination.entity.Levels;
import com.adalab.examination.mapper.LevelsMapper;
import com.adalab.examination.service.LevelsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class LevelsServiceImpl extends ServiceImpl<LevelsMapper, Levels> implements LevelsService {
}
