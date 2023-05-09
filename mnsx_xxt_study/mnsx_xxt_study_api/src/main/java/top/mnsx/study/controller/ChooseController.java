package top.mnsx.study.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.mnsx.model.RestResponse;
import top.mnsx.study.po.Choose;
import top.mnsx.study.service.ChooseService;

@RestController
@RequestMapping("/choose")
public class ChooseController {

    @Autowired
    private ChooseService chooseService;

    @PostMapping("/{courseId}/{userId}")
    public RestResponse<Choose> chooseCourse(@PathVariable("courseId") Integer courseId, @PathVariable("userId") Integer userId) {
        return chooseService.chooseCourse(courseId, userId);
    }
}
