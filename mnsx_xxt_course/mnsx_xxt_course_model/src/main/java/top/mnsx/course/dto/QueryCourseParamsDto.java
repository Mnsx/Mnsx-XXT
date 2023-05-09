package top.mnsx.course.dto;

import lombok.Data;
import lombok.ToString;

/**
 * 查询课程dto
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Data
@ToString
public class QueryCourseParamsDto {
    //审核状态
    private String auditStatus;
    //课程名称
    private String courseName;
    // 分类编号
    private String categoryId;
}
