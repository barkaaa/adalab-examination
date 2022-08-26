package com.adalab.examination.service.impl;

import com.adalab.examination.entity.Test;
import com.adalab.examination.mapper.TestMapper;
import com.adalab.examination.service.TestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Karl
 * @since 2022-08-26
 */
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements TestService {

}
