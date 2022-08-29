package com.adalab.examination.service;

import com.adalab.examination.entity.Challenge;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Karl
 * @since 2022-08-29
 */
public interface ChallengeService extends IService<Challenge> {

    void saveWithType(Challenge challenge);
}
