package top.mnsx.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.mnsx.course.service.CoursePublishService;
import top.mnsx.model.RestResponse;

@RestController
@RequestMapping("/publish")
public class CoursePublishController {
    @Autowired
    private CoursePublishService coursePublishService;

    @GetMapping("/course-audit/commit/{courseId}")
    public RestResponse commitAudit(@PathVariable("courseId") Integer courseId) {
        coursePublishService.commitAudit(courseId);
        return RestResponse.success(null);
    }

    @PostMapping("/{courseId}")
    public void publish(@PathVariable("courseId") Integer courseId) {
        coursePublishService.publish(courseId);
    }
}
