package com.adalab.examination.service.impl;

import com.adalab.examination.entity.Challenge;
import com.adalab.examination.entity.ChallengeType;
import com.adalab.examination.mapper.ChallengeMapper;
import com.adalab.examination.service.ChallengeService;
import com.adalab.examination.service.ChallengeTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Karl
 * @since 2022-08-29
 */
@Service
public class ChallengeServiceImpl extends ServiceImpl<ChallengeMapper, Challenge> implements ChallengeService {
    @Autowired
    ChallengeTypeService challengeTypeService;
    @Override
    @Transactional
    public void saveWithType(Challenge challenge) {
        this.save(challenge);
        ChallengeType challengeType = new ChallengeType();
        challengeType.setStage(challenge.getStage());
        challengeType.setType(2);
        challengeTypeService.save(challengeType);
    }
}
