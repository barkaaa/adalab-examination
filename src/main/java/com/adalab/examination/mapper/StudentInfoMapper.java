package com.adalab.examination.mapper;

import com.adalab.examination.entity.StudentInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Karl
 * @since 2022-08-27
 */
public interface StudentInfoMapper extends BaseMapper<StudentInfo> {

    List<StudentInfo>selectWeekDataPage(@Param("start") int start, @Param("pageSize") int pageSize);

    List<StudentInfo>selectWeekData();
}
