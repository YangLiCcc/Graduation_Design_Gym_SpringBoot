package com.yang.springboot.service.impl;

import com.yang.springboot.entity.Course;
import com.yang.springboot.mapper.CourseDao;
import com.yang.springboot.service.CourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LambCcc
 * @since 2022-04-14
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseDao, Course> implements CourseService {

}
