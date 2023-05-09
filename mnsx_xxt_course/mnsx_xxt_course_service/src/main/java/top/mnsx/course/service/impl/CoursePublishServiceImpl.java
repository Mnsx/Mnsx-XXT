package top.mnsx.course.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import top.mnsx.course.dto.CatalogueDto;
import top.mnsx.course.dto.CourseInfoDto;
import top.mnsx.course.dto.CoursePreviewDto;
import top.mnsx.course.mapper.CourseInfoMapper;
import top.mnsx.course.mapper.CoursePublishPreMapper;
import top.mnsx.model.po.CourseInfo;
import top.mnsx.course.po.CoursePublish;
import top.mnsx.course.po.CoursePublishPre;
import top.mnsx.course.po.CourseSale;
import top.mnsx.course.service.*;
import top.mnsx.course.mapper.CoursePublishMapper;
import org.springframework.stereotype.Service;
import top.mnsx.exception.XXTException;

import javax.annotation.Resource;
import java.util.List;

/**
* @author Mnsx_x
* @description 针对表【course_publish】的数据库操作Service实现
* @createDate 2023-04-15 01:31:45
*/
@Service
public class CoursePublishServiceImpl extends ServiceImpl<CoursePublishMapper, CoursePublish>
    implements CoursePublishService{

    @Autowired
    private CourseInfoService courseInfoService;
    @Autowired
    private CatalogueService catalogueService;
    @Autowired
    private CourseSaleService courseSaleService;
    @Resource
    private CoursePublishPreMapper coursePublishPreMapper;
    @Resource
    private CourseInfoMapper courseInfoMapper;
    @Autowired
    private CourseCategoryService courseCategoryService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void commitAudit(Integer courseId) {
        CourseInfo courseInfo = courseInfoMapper.selectById(courseId);
        if (courseInfo == null) {
            XXTException.cast("课程不存在");
        }

        // 审核状态
        Integer auditStatus = courseInfo.getAuditStatus();

        // 如果已提交不允许重复提交
        if (auditStatus == 1) {
            XXTException.cast("已提交，不允许重复提交");
        }

        CoursePublishPre coursePublishPre = new CoursePublishPre();
        BeanUtils.copyProperties(courseInfo, coursePublishPre);

        // 查询课程计划
        List<CatalogueDto> catalogueTree = catalogueService.findCatalogueTree(courseId);
        coursePublishPre.setCourseCatalogue(JSON.toJSONString(catalogueTree));

        // 查询课程营销信息
        CourseSale courseSale = courseSaleService.getById(courseId);
        coursePublishPre.setCourseSale(JSON.toJSONString(courseSale));

        // 状态修改为已提交
        coursePublishPre.setAuditStatus(1);

        // 设置编号
        coursePublishPre.setId(courseId);

        // 设置分类名
        String firstName = courseCategoryService.getCategoryNameById(courseInfo.getFirstCategory());
        String secondName = courseCategoryService.getCategoryNameById(courseInfo.getSecondCategory());
        coursePublishPre.setFirstName(firstName);
        coursePublishPre.setSecondName(secondName);

        CoursePublishPre result = coursePublishPreMapper.selectById(courseId);
        if (result == null) {
            coursePublishPreMapper.insert(coursePublishPre);
        } else {
            coursePublishPreMapper.updateById(coursePublishPre);
        }

        // 设置信息状态已提交
        courseInfo.setAuditStatus(1);
        courseInfoMapper.updateById(courseInfo);
    }

    @Override
    public void publish(Integer courseId) {
        CoursePublishPre coursePublishPre = coursePublishPreMapper.selectById(courseId);
        if (coursePublishPre == null) {
            XXTException.cast("课程没有提交审核，无法发布");
        }

        Integer auditStatus = coursePublishPre.getAuditStatus();
        if (auditStatus != 2) {
            XXTException.cast("课程审核未通过，不允许发布");
        }

        // 向课程发布表写入数据
        CoursePublish coursePublish = new CoursePublish();
        BeanUtils.copyProperties(coursePublishPre, coursePublish);
        this.saveOrUpdate(coursePublish);

        // 写入消息表
        rabbitTemplate.convertAndSend("course.publish", String.valueOf(courseId));

        // 将预发布表中的内容删除
        coursePublishPreMapper.deleteById(courseId);
    }

    @Override
    public CoursePreviewDto getCoursePreviewInfo(Integer courseId) {
        CoursePreviewDto coursePreviewDto = new CoursePreviewDto();
        // 添加课程信息和营销信息
        CourseInfoDto courseInfo = courseInfoService.getCourseInfoById(courseId);
        coursePreviewDto.setCourseInfoDto(courseInfo);
        // 课程目录
        List<CatalogueDto> catalogueTree = catalogueService.findCatalogueTree(courseId);
        coursePreviewDto.setCatalogues(catalogueTree);

        return coursePreviewDto;
    }
}




