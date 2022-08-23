package com.adalab.examination.service.impl;

import com.adalab.examination.entity.Student;
import com.adalab.examination.mapper.StudentMapper;
import com.adalab.examination.service.StudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Karl
 * @since 2022-08-23
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

}
