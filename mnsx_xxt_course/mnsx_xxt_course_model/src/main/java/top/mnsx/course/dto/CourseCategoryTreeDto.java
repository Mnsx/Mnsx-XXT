package top.mnsx.course.dto;

import lombok.Data;
import top.mnsx.course.po.CourseCategory;

import java.util.List;

/**
 * 课程分类树dto
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Data
public class CourseCategoryTreeDto extends CourseCategory {

    List childrenTreeNodes;
}
