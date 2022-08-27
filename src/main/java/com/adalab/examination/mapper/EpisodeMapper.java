package com.adalab.examination.mapper;

import com.adalab.examination.entity.Episode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Karl
 * @since 2022-08-26
 */
@Mapper
public interface EpisodeMapper extends BaseMapper<Episode> {

    int getCount();

    void refreshIncreaseId(int id);

    void upAfterId(int id);

    void downAfterId(int id);

    void insertById(Episode episode);


}
