package top.mnsx.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.mnsx.course.dto.AddCourseDto;
import top.mnsx.course.dto.CourseInfoDto;
import top.mnsx.course.dto.EditCourseDto;
import top.mnsx.course.dto.QueryCourseParamsDto;
import top.mnsx.model.po.CourseInfo;
import top.mnsx.course.service.CourseInfoService;
import top.mnsx.model.PageParams;
import top.mnsx.model.PageResult;
import top.mnsx.model.RestResponse;

/**
 * 课程基本信息controller
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@RequestMapping("/course-info")
@RestController
public class CourseInfoController {
    @Autowired
    private CourseInfoService courseInfoService;

    /**
     * 分页展示
     * @param pageParams 分页参数
     * @param queryCourseParams 课程搜索参数
     * @return pageResult
     */
    @PostMapping("/list")
    public RestResponse<PageResult<CourseInfo>> list(PageParams pageParams, @RequestBody QueryCourseParamsDto queryCourseParams) {
        return RestResponse.success(courseInfoService.queryCourseInfoList(pageParams, queryCourseParams));
    }

    /**
     * 创建课程信息
     * @param addCourseDto 添加课程参数
     * @return 课程信息Dto
     */
    @PostMapping
    public RestResponse<CourseInfoDto> createCourseInfo(@RequestBody @Validated AddCourseDto addCourseDto) {
        return RestResponse.success(courseInfoService.createCourseInfo(addCourseDto));
    }

    /**
     * 通过Id获取课程基本信息
     * @param courseId 课程id
     * @return 返回课程基本信息Dto
     */
    @GetMapping("/{id}")
    public RestResponse<CourseInfoDto> getCourseInfoById(@PathVariable("id") Integer courseId) {
        return RestResponse.success(courseInfoService.getCourseInfoById(courseId));
    }

    /**
     * 修改课程基本信息
     * @param editCourseDto 修改信息Dto
     * @return 返回课程基本信息Dto
     */
    @PutMapping
    public RestResponse<CourseInfoDto> modifyCourseInfo(@RequestBody EditCourseDto editCourseDto) {
        return RestResponse.success(courseInfoService.updateCourseInfo(editCourseDto));
    }

    @PutMapping("/status")
    public RestResponse<CourseInfo> modifyCourseStatus(@RequestBody EditCourseDto editCourseDto) {
        return courseInfoService.changeStatus(editCourseDto);
    }
}
