package com.yang.springboot.controller;

import com.yang.springboot.common.lang.Result;
import com.yang.springboot.entity.Course;
import com.yang.springboot.param.CourseParam;
import com.yang.springboot.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LambCcc
 * @since 2022-04-14
 */
@RestController
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/get/page")
    public Result getCoursePage(@RequestParam Integer currentPage,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String coachName) {
        return courseService.getCoursePage(currentPage, pageSize, coachName);
    }

    @PostMapping("/insert")
    public Result insertCourse(@RequestBody CourseParam insertParam) {
        return courseService.insertCourse(insertParam);
    }

    @PostMapping("/update")
    public Result updateCourse(@RequestBody Course course) {
        return courseService.updateCourse(course);
    }

    @PostMapping("/delete/{id}")
    public Result deleteCourse(@PathVariable Long id) {
        return courseService.deleteCourseById(id);
    }

}
