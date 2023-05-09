package top.mnsx.course.service;


import top.mnsx.course.dto.AddCourseDto;
import top.mnsx.course.dto.CourseInfoDto;
import top.mnsx.course.dto.EditCourseDto;
import top.mnsx.course.dto.QueryCourseParamsDto;
import top.mnsx.model.RestResponse;
import top.mnsx.model.po.CourseInfo;
import top.mnsx.model.PageParams;
import top.mnsx.model.PageResult;

/**
 * 课程基本信息service
 * @Author Mnsx_x xx1527030652@gmail.com
 */
public interface CourseInfoService {

    /**
     * 课程查询
     * @param params 分页参数
     * @param queryCourseParamsDto 查询课程参数
     * @return 分页数据
     */
    public PageResult<CourseInfo> queryCourseInfoList(PageParams params, QueryCourseParamsDto queryCourseParamsDto);

    /**
     * 新增课程
     * @param addCourseDto 添加参数
     * @return 相应参数
     */
    public CourseInfoDto createCourseInfo(AddCourseDto addCourseDto);

    /**
     * 修改课程信息
     * @param editCourseDto 参数
     * @return 信息
     */
    public CourseInfoDto updateCourseInfo(EditCourseDto editCourseDto);

    /**
     * 根据课程编号获取课程
     * @param courseId 课程编号
     * @return CourseInfoDto
     */
    public CourseInfoDto getCourseInfoById(Integer courseId);

    RestResponse<CourseInfo> changeStatus(EditCourseDto editCourseDto);
}
