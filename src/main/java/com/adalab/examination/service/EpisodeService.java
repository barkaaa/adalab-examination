package com.adalab.examination.service;

import com.adalab.examination.entity.Episode;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Karl
 * @since 2022-08-26
 */
public interface EpisodeService extends IService<Episode> {
    void delete(int id);

    void insert(Episode episode);
}
