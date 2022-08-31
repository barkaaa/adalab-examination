package com.adalab.examination.service.impl;

import com.adalab.examination.entity.Episode;
import com.adalab.examination.mapper.EpisodeMapper;
import com.adalab.examination.service.EpisodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Karl
 * @since 2022-08-26
 */
@Service
public class EpisodeServiceImpl extends ServiceImpl<EpisodeMapper, Episode> implements EpisodeService {
    final
    EpisodeMapper episodeMapper;

    public EpisodeServiceImpl(EpisodeMapper episodeMapper) {
        this.episodeMapper = episodeMapper;
    }

    @Override
    public void delete(int id) {
        removeById(id);
        episodeMapper.downAfterId(id);
        episodeMapper.refreshIncreaseId(episodeMapper.getCount() + 1);
    }

    @Override
    public void insert(Episode episode) {
        if (getById(episode.getId()) != null) {
            episodeMapper.upAfterId(episode.getId());
        }
        episodeMapper.insertById(episode);
        episodeMapper.refreshIncreaseId(episodeMapper.getCount() + 1);
    }

    public int nums() {
        return episodeMapper.getCount();
    }

}
