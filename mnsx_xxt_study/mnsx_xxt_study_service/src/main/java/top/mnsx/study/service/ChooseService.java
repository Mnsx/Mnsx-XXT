package top.mnsx.study.service;

import top.mnsx.model.RestResponse;
import top.mnsx.study.po.Choose;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Mnsx_x
* @description 针对表【study_choose】的数据库操作Service
* @createDate 2023-04-18 16:56:17
*/
public interface ChooseService extends IService<Choose> {

    /**
     * 选课
     * @param courseId 课程编号
     * @param userId 用户编号
     * @return Choose
     */
    RestResponse<Choose> chooseCourse(Integer courseId, Integer userId);
}
