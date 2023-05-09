package top.mnsx.course.service.impl;

import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.mnsx.course.dto.AddCourseDto;
import top.mnsx.course.dto.CourseInfoDto;
import top.mnsx.course.dto.EditCourseDto;
import top.mnsx.course.dto.QueryCourseParamsDto;
import top.mnsx.course.enums.AuditStatusEnum;
import top.mnsx.course.enums.IfChargeEnum;
import top.mnsx.course.mapper.CourseCategoryMapper;
import top.mnsx.course.mapper.CourseInfoMapper;
import top.mnsx.course.mapper.CourseSaleMapper;
import top.mnsx.course.po.CourseCategory;
import top.mnsx.model.RestResponse;
import top.mnsx.model.po.CourseInfo;
import top.mnsx.course.po.CourseSale;
import top.mnsx.course.service.CourseInfoService;
import top.mnsx.course.service.CourseSaleService;
import top.mnsx.enums.StatusEnum;
import top.mnsx.exception.XXTException;
import top.mnsx.model.PageParams;
import top.mnsx.model.PageResult;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Slf4j
@Service
public class CourseInfoServiceImpl implements CourseInfoService {
    @Resource
    private CourseInfoMapper courseInfoMapper;
    @Resource
    private CourseSaleMapper courseSaleMapper;
    @Resource
    private CourseCategoryMapper courseCategoryMapper;
    @Autowired
    private CourseSaleService courseSaleService;

    @Override
    public PageResult<CourseInfo> queryCourseInfoList(PageParams params, QueryCourseParamsDto queryCourseParamsDto) {
        LambdaQueryWrapper<CourseInfo> queryWrapper = new LambdaQueryWrapper<>();

        // 拼接查询条件
        // 根据课程名称模糊查询
        queryWrapper.like(StringUtils.isNotEmpty(queryCourseParamsDto.getCourseName()),
                        CourseInfo::getCourseName,
                        queryCourseParamsDto.getCourseName());

        queryWrapper.eq(queryCourseParamsDto.getCategoryId() != null, CourseInfo::getFirstCategory, queryCourseParamsDto.getCategoryId());

        // 根据课程审核状态
        queryWrapper.eq(StringUtils.isNotEmpty(queryCourseParamsDto.getAuditStatus()),
                CourseInfo::getAuditStatus,
                queryCourseParamsDto.getAuditStatus());

        // 分页参数
        Page<CourseInfo> page = new Page<>(params.getPageNum(), params.getPageSize());

        // 分页查询
        Page<CourseInfo> pageResult = courseInfoMapper.selectPage(page, queryWrapper);

        // 封装返回数据
        // 数据
        List<CourseInfo> items = pageResult.getRecords();
        // 总记录数
        long total = pageResult.getTotal();

        return new PageResult<>(items, total, params.getPageNum(), params.getPageSize());
    }

    @Override
    @Transactional
    public CourseInfoDto createCourseInfo(AddCourseDto dto) {
        CourseInfo courseInfo = new CourseInfo();
        // 将传入dto的数据设置到courseBase中
        BeanUtils.copyProperties(dto, courseInfo);

        // 审核状态未提交
        courseInfo.setAuditStatus(AuditStatusEnum.UNAUDITED.getCode());
        // 发布状态未发布
        courseInfo.setStatus(StatusEnum.OFF.getCode());

        // 向课程信息表插入数据
        int insert = courseInfoMapper.insert(courseInfo);
        // 获取课程id
        Integer courseId = courseInfo.getId();

        CourseSale courseSale = new CourseSale();

        // 准备返回至
        BeanUtils.copyProperties(dto, courseSale);
        courseSale.setId(courseId);

        // 向课程营销表注入数据
        int result = saveCourseMarket(dto, courseSale);

        if (insert <= 0 || result <= 0) {
            throw new RuntimeException("添加课程失败");
        }

        // 组装返回结构
        CourseInfoDto courseInfoDto = new CourseInfoDto();
        BeanUtils.copyProperties(courseInfo, courseInfoDto);
        BeanUtils.copyProperties(courseSale, courseInfoDto);
        return courseInfoDto;
    }

    @Override
    @Transactional
    public CourseInfoDto updateCourseInfo(EditCourseDto editCourseDto) {
        // 课程ID
        Integer courseId = editCourseDto.getId();
        CourseInfo courseInfo = courseInfoMapper.selectById(courseId);
        if (courseInfo == null) {
            XXTException.cast("课程不存在");
        }

        // 封装基本信息的数据
        BeanUtils.copyProperties(editCourseDto, courseInfo);
        courseInfo.setUpdateTime(new Date());

        courseInfoMapper.updateById(courseInfo);

        // 封装营销信息的数据
        CourseSale courseSale = new CourseSale();
        BeanUtils.copyProperties(editCourseDto, courseSale);


        int result = saveCourseMarket(editCourseDto, courseSale);

        return this.getCourseInfoById(courseId);
    }

    @Override
    @Transactional
    public CourseInfoDto getCourseInfoById(Integer courseId) {
        // 基础信息
        CourseInfo courseInfo = courseInfoMapper.selectById(courseId);

        // 营销信息
        CourseSale courseSale = courseSaleMapper.selectById(courseId);

        CourseInfoDto courseInfoDto = new CourseInfoDto();
        BeanUtils.copyProperties(courseInfo, courseInfoDto);
        BeanUtils.copyProperties(courseSale, courseInfoDto);

        // 根据课程分类的id查询分类的名称
        String firstCategoryName = courseInfo.getFirstCategory();
        String secondCategoryName = courseInfo.getSecondCategory();

        CourseCategory firstCategory = courseCategoryMapper.selectById(firstCategoryName);
        CourseCategory secondCategory = courseCategoryMapper.selectById(secondCategoryName);
        if (firstCategory != null) {
            // 分类名称
            String firstName = firstCategory.getCategoryName();
            courseInfoDto.setFirstCategory(firstName);
        }
        if (secondCategory != null) {
            // 分类名称
            String secondName = secondCategory.getCategoryName();
            courseInfoDto.setSecondCategory(secondName);
        }

        return courseInfoDto;
    }

    @Override
    public RestResponse<CourseInfo> changeStatus(EditCourseDto editCourseDto) {
        Integer id = editCourseDto.getId();
        CourseInfo courseInfo = courseInfoMapper.selectById(id);
        Integer status = editCourseDto.getStatus();
        courseInfo.setStatus(status);
        courseInfoMapper.updateById(courseInfo);
        return RestResponse.success(courseInfo);
    }

    private int saveCourseMarket(AddCourseDto dto, CourseSale courseSale) {
        // 如果课程收费，价格必须输入
        Integer charge = dto.getIfCharge();
        if (charge.equals(IfChargeEnum.CHARGE.getCode())) {
            if (courseSale.getPrice() == null || courseSale.getPrice() <= 0) {
                XXTException.cast("课程为收费，但是价格为空");
//                throw new XXTException("课程为收费，但是价格为空");
            }
        }

        // 保存
        boolean b = courseSaleService.saveOrUpdate(courseSale);

        return b ? 1 : 0;
    }
}
