package top.mnsx.course.dto;

import lombok.Data;

/**
 * 编辑课程dto
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Data
public class EditCourseDto extends AddCourseDto {
    private Integer id;

    private Integer status;
}
