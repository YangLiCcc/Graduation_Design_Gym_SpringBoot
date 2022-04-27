package com.yang.springboot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yang.springboot.common.Constants;
import com.yang.springboot.common.lang.Result;
import com.yang.springboot.entity.Course;
import com.yang.springboot.entity.User;
import com.yang.springboot.mapper.CourseDao;
import com.yang.springboot.param.CourseParam;
import com.yang.springboot.service.CourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.springboot.service.UserService;
import com.yang.springboot.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    private final UserService userService;

    @Autowired
    public CourseServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Result getCoursePage(Integer currentPage, Integer pageSize, String coachName) {
        Page<Course> page = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Course> courseLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotEmpty(coachName)) {
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.like(User::getNickname, coachName);
            List<Long> userIds = userService.listUserIds(userLambdaQueryWrapper);
            courseLambdaQueryWrapper.eq(Course::getCouchId, userIds);
        }

        return new Result(Constants.CODE_200, "获取分页信息成功!", page(page, courseLambdaQueryWrapper));
    }

    @Override
    public Result insertCourse(CourseParam insertParam) {
        User userInfo = UserUtils.getUserInfo();
        Course course = new Course();
        BeanUtil.copyProperties(insertParam, course, true);
        course.setCreatedBy(userInfo.getId());
        course.setCreatedTime(LocalDateTime.now());
        if (save(course)) {
            return new Result(Constants.CODE_200, "新增课程成功!", null);
        }

        return new Result(Constants.CODE_500, "新增课程失败!", null);
    }

    @Override
    public Result updateCourse(Course course) {
        User userInfo = UserUtils.getUserInfo();
        course.setModifiedBy(userInfo.getId());
        course.setModifiedTime(LocalDateTime.now());
        if (updateById(course)) {
            return new Result(Constants.CODE_200, "更新课程成功!", null);
        }

        return new Result(Constants.CODE_500, "更新课程失败!", null);
    }

    @Override
    public Result deleteCourseById(Long id) {
        User userInfo = UserUtils.getUserInfo();
        Course course = getById(id);
        course.setDeletedBy(userInfo.getId());
        course.setDeletedTime(LocalDateTime.now());
        course.setIsDeleted(true);
        course.setStatus(false);
        if (updateById(course)) {
            return new Result(Constants.CODE_200, "删除课程成功!", null);
        }

        return new Result(Constants.CODE_500, "删除课程失败!", null);
    }
}
