package top.mnsx.course.dto;

import lombok.Data;

import java.util.List;

@Data
public class CoursePreviewDto {
    // 课程基本信息、课程营销信息
    private CourseInfoDto courseInfoDto;

    // 课程目录信息
    private List<CatalogueDto> catalogues;


}
