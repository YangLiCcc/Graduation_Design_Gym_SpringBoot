package com.yang.springboot.service;

import com.yang.springboot.common.lang.Result;
import com.yang.springboot.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.springboot.param.CourseParam;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LambCcc
 * @since 2022-04-14
 */
public interface CourseService extends IService<Course> {

    Result getCoursePage(Integer currentPage, Integer pageSize, String coachName);

    Result insertCourse(CourseParam insertParam);

    Result updateCourse(Course course);

    Result deleteCourseById(Long id);
}
