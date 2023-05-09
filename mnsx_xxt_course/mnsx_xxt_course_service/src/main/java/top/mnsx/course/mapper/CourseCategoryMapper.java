package top.mnsx.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.mnsx.course.dto.CourseCategoryTreeDto;
import top.mnsx.course.po.CourseCategory;

import java.util.List;

/**
* @author Mnsx_x
* @description 针对表【course_category】的数据库操作Mapper
* @createDate 2023-04-06 22:04:31
* @Entity top.mnsx.content.po.CourseCategory
*/
public interface CourseCategoryMapper extends BaseMapper<CourseCategory> {

    List<CourseCategoryTreeDto> selectTreeNodes(String id);
}




