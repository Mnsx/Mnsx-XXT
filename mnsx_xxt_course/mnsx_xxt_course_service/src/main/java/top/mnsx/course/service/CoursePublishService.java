package top.mnsx.course.service;

import top.mnsx.course.dto.CoursePreviewDto;
import top.mnsx.course.po.CoursePublish;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Mnsx_x
* @description 针对表【course_publish】的数据库操作Service
* @createDate 2023-04-15 01:31:45
*/
public interface CoursePublishService extends IService<CoursePublish> {

    /**
     * 提交审核
     * @param courseId 课程编号
     */
    void commitAudit(Integer courseId);

    /**
     * 发布课程
     * @param courseId 课程id
     */
    void publish(Integer courseId);

    /**
     * 根据课程id获取预览课程数据
     * @param courseId 课程编号
     * @return CoursePreviewDto
     */
     CoursePreviewDto getCoursePreviewInfo(Integer courseId);
}
