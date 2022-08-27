package com.adalab.examination.service.impl;

import com.adalab.examination.entity.StudentInfo;
import com.adalab.examination.mapper.StudentInfoMapper;
import com.adalab.examination.service.StudentInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Karl
 * @since 2022-08-27
 */
@Service
public class StudentInfoServiceImpl extends ServiceImpl<StudentInfoMapper, StudentInfo> implements StudentInfoService {

}
