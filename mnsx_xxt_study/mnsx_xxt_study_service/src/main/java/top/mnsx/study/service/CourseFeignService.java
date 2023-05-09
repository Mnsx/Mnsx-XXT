package top.mnsx.study.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.mnsx.model.RestResponse;
import top.mnsx.model.po.CourseInfo;

@Component
@FeignClient("course-api")
public interface CourseFeignService {
    @GetMapping("/course/course-info/{id}")
    public RestResponse<CourseInfo> getCourseInfoById(@PathVariable("id") Integer courseId);
}
