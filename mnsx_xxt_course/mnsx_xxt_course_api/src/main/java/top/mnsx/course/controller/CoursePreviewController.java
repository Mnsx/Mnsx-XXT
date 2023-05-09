package top.mnsx.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.mnsx.course.dto.CoursePreviewDto;
import top.mnsx.course.service.CoursePublishService;
import top.mnsx.model.RestResponse;

@RestController
@RequestMapping("/preview")
public class CoursePreviewController {
    @Autowired
    private CoursePublishService coursePublishService;

    @GetMapping("/course/{courseId}")
    public RestResponse<CoursePreviewDto> getMediaUrlByMediaId(@PathVariable("courseId") Integer courseId) {
        return RestResponse.success(coursePublishService.getCoursePreviewInfo(courseId));
    }
}
