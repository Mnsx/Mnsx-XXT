package top.mnsx.study.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import top.mnsx.enums.IfChargeEnum;
import top.mnsx.model.RestResponse;
import top.mnsx.model.po.CourseInfo;
import top.mnsx.model.po.User;
import top.mnsx.study.po.Choose;
import top.mnsx.study.service.AuthFeignService;
import top.mnsx.study.service.ChooseService;
import top.mnsx.study.mapper.ChooseMapper;
import org.springframework.stereotype.Service;
import top.mnsx.study.service.CourseFeignService;

import javax.annotation.Resource;

/**
* @author Mnsx_x
* @description 针对表【study_choose】的数据库操作Service实现
* @createDate 2023-04-18 16:56:17
*/
@Service
public class ChooseServiceImpl extends ServiceImpl<ChooseMapper, Choose>
    implements ChooseService {

    @Resource
    private AuthFeignService authFeignService;
    @Resource
    private CourseFeignService courseFeignService;
    @Resource
    private ChooseMapper chooseMapper;

    @Override
    public RestResponse<Choose> chooseCourse(Integer courseId, Integer userId) {

        // 判断用户是否存在
        RestResponse<User> userRestResponse = authFeignService.getUserById(userId);
        User user = userRestResponse.getResult();

        // 判断课程是否存在
        RestResponse<CourseInfo> courseInfoRestResponse = courseFeignService.getCourseInfoById(courseId);
        CourseInfo courseInfo = courseInfoRestResponse.getResult();

        // 判断课程是否免费
        Integer ifCharge = courseInfo.getIfCharge();
        if (!ifCharge.equals(IfChargeEnum.NO_CHARGE.getCode())) {

            // 付费判断用户是否有足够金钱

            // 足够直接通过

            // 不够，返回异常提示用户充值
        }
        // 免费直接通过

        // 准备数据
        Choose choose = new Choose();
        choose.setCourseId(courseId);
        choose.setUserId(userId);
        choose.setOrderType(ifCharge);
        choose.setStatus(0);

        // 将数据添加到用户的课程表中
        chooseMapper.insert(choose);

        return RestResponse.success(choose);
    }
}




